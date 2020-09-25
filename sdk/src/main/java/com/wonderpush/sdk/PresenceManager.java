package com.wonderpush.sdk;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PresenceManager {
    public static class PresencePayload {
        private Date fromDate;
        private Date untilDate;
        private long elapsedTime;
        public PresencePayload(Date fromDate, Date untilDate) {
            this.fromDate = fromDate;
            this.untilDate = untilDate;
            this.elapsedTime = untilDate.getTime() - fromDate.getTime();
        }

        public Date getFromDate() {
            return fromDate;
        }

        public Date getUntilDate() {
            return untilDate;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public JSONObject toJSONObject() throws JSONException {
            JSONObject result = new JSONObject();
            result.put("fromDate", fromDate.getTime());
            result.put("untilDate", untilDate.getTime());
            result.put("elapsedTime", getElapsedTime());
            return result;
        }
    }
    public interface PresenceManagerAutoRenewDelegate {
        void autoRenewPresence(PresenceManager presenceManager, PresencePayload presence);
    }


    @Nullable
    private PresenceManagerAutoRenewDelegate autoRenewDelegate;
    private long anticipatedTime;
    private long safetyMarginTime;
    @Nullable
    private PresencePayload lastPresencePayload;
    @Nullable
    private HandlerThread handlerThread;
    @Nullable
    private Handler handler;
    private Runnable autoRenewRunnable = new Runnable() {
        @Override
        public void run() {
            extendPresence();
            synchronized (PresenceManager.this) {
                if (handler != null) {
                    handler.postDelayed(autoRenewRunnable, getAutoRenewTimeInterval());
                }
            }
        }
    };

    public PresenceManager(@Nullable PresenceManagerAutoRenewDelegate delegate, long anticipatedTime, long safetyMarginTime) {
        this.autoRenewDelegate = delegate;
        this.anticipatedTime = anticipatedTime;
        this.safetyMarginTime = Math.max(safetyMarginTime, 100);
    }

    private long getAutoRenewTimeInterval() {
        return Math.round(safetyMarginTime / 10);
    }

    private boolean autoRenew() {
        return this.autoRenewDelegate != null;
    }

    private void startAutoRenew() throws InterruptedException {
        synchronized (this) {
            if (handlerThread != null) return;
            handlerThread = new HandlerThread("presence");
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            handler = new Handler(looper);
            handler.postDelayed(autoRenewRunnable, getAutoRenewTimeInterval());
        }
    }

    private void stopAutoRenew() {
        synchronized (this) {
            if (handlerThread == null) return;
            if (handler != null) handler.removeCallbacks(this.autoRenewRunnable);
            handlerThread.quit();
            handlerThread.interrupt();
            handlerThread = null;
            handler = null;
        }
    }

    public long getAnticipatedTime() {
        return anticipatedTime;
    }

    public long getSafetyMarginTime() {
        return safetyMarginTime;
    }

    @NonNull
    public PresencePayload presenceDidStart() throws InterruptedException {
        stopAutoRenew();
        if (this.autoRenew()) startAutoRenew();

        Date startDate = new Date();
        Date untilDate = new Date(startDate.getTime() + anticipatedTime);
        lastPresencePayload = new PresencePayload(startDate, untilDate);
        return lastPresencePayload;
    }

    @NonNull
    public PresencePayload presenceWillStop() {
        stopAutoRenew();
        Date now = new Date();
        Date fromDate = new Date((lastPresencePayload != null ? lastPresencePayload.getFromDate() : now).getTime());
        PresencePayload payload = new PresencePayload(fromDate, now);
        lastPresencePayload = null;
        return payload;
    }

    public boolean isCurrentlyPresent() {
        if (lastPresencePayload == null) return false;
        return lastPresencePayload.getUntilDate().getTime() - new Date().getTime() > 0;
    }

    private void extendPresence() {
        Date now = new Date();
        long timeUntilPresenceEnds = lastPresencePayload != null ? lastPresencePayload.getUntilDate().getTime() - now.getTime() : 0;

        // Not time to update yet.
        if (timeUntilPresenceEnds > safetyMarginTime) return;

        // Compute fromDate
        Date fromDate;
        if (timeUntilPresenceEnds < 0) {
            // When we're past 'untilDate', it's a new presence. Override 'fromDate'
            fromDate = now;
        } else {
            fromDate = lastPresencePayload != null ? lastPresencePayload.getFromDate() : now;
        }

        // Compute new 'untilDate'
        Date untilDate = new Date(now.getTime() + anticipatedTime);

        // Payload
        lastPresencePayload = new PresencePayload(fromDate, untilDate);

        // Tell the delegate
        if (autoRenewDelegate != null) {
            autoRenewDelegate.autoRenewPresence(this, lastPresencePayload);
        }
    }
}
