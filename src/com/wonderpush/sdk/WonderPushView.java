package com.wonderpush.sdk;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wonderpush.sdk.R;

/**
 * An Android view that displays WonderPush content.
 * A WonderPushView will hold a WebView pointing to WonderPush web content.
 */
@SuppressLint("SetJavaScriptEnabled")
class WonderPushView extends FrameLayout {

    private static final String TAG = WonderPush.TAG;

    OnStateListener mStateListener;
    WebView mWebView;
    ViewGroup mErrorLayout;
    TextView mMessageView;
    ProgressBar mProgressBar;
    WonderPushWebCallbackHandler mWebCallbackHandler = new WonderPushWebCallbackHandler();
    ImageButton mCloseButton;
    boolean mUseCloseButton;
    WonderPushView mSubview;
    Uri mOutputFileUri;
    UserInterfaceState mUserInterfaceState;
    ValueCallback<Uri> mUploadMessage;
    String mInitialResource;
    WonderPush.RequestParams mInitialRequestParams;
    boolean mIsPreloading;
    protected boolean isLoginSource;

    public WonderPushView(Context context) {
        super(context);
        init();
    }

    public WonderPushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WonderPushView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Create the error layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mErrorLayout = (ViewGroup) inflater.inflate(R.layout.wonderpush_error_layout, this, false);
        if (mErrorLayout != null) {
            // Configure refresh button
            Button refreshButton = (Button) mErrorLayout.findViewById(R.id.wonderpush_retry_button);
            if (null != refreshButton) {
                refreshButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (null == mWebView.getUrl()) {
                            setResource(mInitialResource);
                        } else {
                            mWebView.reload();
                        }
                    }
                });
            }
            // Configure cancel button
            Button cancelButton = (Button) mErrorLayout.findViewById(R.id.wonderpush_cancel_button);
            if (null != cancelButton) {
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        close();
                    }
                });
            }
            // Get hold of the message view
            mMessageView = (TextView) mErrorLayout.findViewById(R.id.wonderpush_error_message_view);
            addView(mErrorLayout);
        }

        // Create the close button
        mCloseButton = new ImageButton(getContext());
        Drawable closeButtonDrawable = getResources().getDrawable(R.drawable.wonderpush_close_button);
        int closeButtonMargin = (int) 0;
        FrameLayout.LayoutParams closeButtonLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);
        closeButtonLayoutParams.setMargins(0, closeButtonMargin, closeButtonMargin, 0);
        mCloseButton.setLayoutParams(closeButtonLayoutParams);
        mCloseButton.setImageDrawable(closeButtonDrawable);
        mCloseButton.setBackground(null);
        mCloseButton.setPadding(0, 0, 0, 0);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                close();
            }
        });
        addView(mCloseButton);
        mUseCloseButton = true;

        // Create the web view
        mWebView = new WebView(getContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mWebView.setWebViewClient(new WonderPushWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, "javascript Error: " + String.format("%s @ %d: %s", cm.message(), cm.lineNumber(), cm.sourceId()));
                return true;
            }
        });
        // Configure it
        mWebView.setBackgroundColor(0x00000000);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            new Runnable() { // wrapping solely for suppressing the deprecation warning locally
                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    mWebView.getSettings().setDatabasePath(getContext().getDir("databases", Context.MODE_PRIVATE).getPath());
                }
            }.run();
        }
        // Set the medium font size that is normally used in dialogs
        int[] attrs = new int[] { android.R.attr.textSize };
        TypedArray ta = getContext().obtainStyledAttributes(null, attrs, android.R.attr.textAppearanceMedium, android.R.style.TextAppearance_Medium);
        int mediumFontSizePx = Math.round(36 * getContext().getResources().getDisplayMetrics().scaledDensity / getContext().getResources().getDisplayMetrics().density);
        mediumFontSizePx = ta.getDimensionPixelSize(0, mediumFontSizePx);
        ta.recycle();
        int mediumFontSizePt = Math.round(mediumFontSizePx / getContext().getResources().getDisplayMetrics().density);
        mWebView.getSettings().setMinimumLogicalFontSize(mediumFontSizePt);
        // Finally add it for display
        addView(mWebView);

        // Create the animated spinner

        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        mProgressBar.setPadding(padding, padding, padding, padding);
        addView(mProgressBar);
        setUserInterfaceState(new InitialState());
    }

    public void setStateListener(OnStateListener stateListener) {
        mStateListener = stateListener;
    }

    private void setUserInterfaceState(UserInterfaceState state) {
        if (null != mUserInterfaceState)
            mUserInterfaceState.onLeaveState();
        mUserInterfaceState = state;
        state.onEnterState();
        requestLayout();
    }

    public boolean getShowCloseButton() {
        return mUseCloseButton;
    }

    public void setShowCloseButton(boolean value) {
        mUseCloseButton = value;
        if (!mUseCloseButton) {
            mCloseButton.setVisibility(GONE);
        }
    }

    /**
     * Can the webview go back (is its history stack non-empty) ?
     *
     * @return Whether the view can go back or not.
     */
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    /**
     * Makes the WebView go back.
     */
    public void goBack() {
        mWebView.goBack();
    }

    /**
     * Closes this WonderPushView. If the view is attached to the developer hierarchy it will be detached.
     */
    public void close() {
        if (mStateListener != null) {
            mStateListener.onClose();
            mStateListener = null;
        }
    }

    /**
     * Sets the resource for the web content displayed in this WonderPushView's WebView.
     *
     * @param resource
     */
    public void setResource(String resource) {
        setResource(resource, null);
    }

    /**
     * Sets the resource for the web content displayed in this WonderPushView's WebView.
     *
     * @param resource
     * @param params
     */
    public void setResource(String resource, WonderPush.RequestParams params) {
        if (null == resource) {
            Log.e(TAG, "null resource provided to WonderPushView");
            return;
        }
        mInitialResource = resource;
        mInitialRequestParams = params;
        mIsPreloading = false;
        if (null == params)
            params = new WonderPush.RequestParams();

        WonderPushRequestParamsDecorator.decorate(resource, params);

        String url = String.format(Locale.getDefault(), "%s?%s",
                WonderPushUriHelper.getNonSecureAbsoluteUrl(resource),
                params.getURLEncodedString());
        mWebView.loadUrl(url);
    }

    /**
     * Sets the full URL for the web content displayed in this WonderPushView's WebView.
     *
     * @param fullUrl
     *            A full URL, with host.
     */
    public void setFullUrl(String fullUrl) {
        if (fullUrl == null) {
            return;
        }

        Uri parsedUri = Uri.parse(fullUrl);

        if (!WonderPushUriHelper.isAPIUri(parsedUri)) {
            mWebView.loadUrl(fullUrl);
        } else {
            setResource(WonderPushUriHelper.getResource(parsedUri), WonderPushUriHelper.getParams(parsedUri));
        }
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    /**
     * An implementation of {@link android.webkit.WebViewClient} that queries the
     * {@link WonderPushWebCallbackHandler} before loading any URL.
     */
    private class WonderPushWebViewClient extends WebViewClient {

        private boolean mError;
        private boolean mIsLoading;
        private Timer mWebviewTimer;
        private Handler mMainThreadHandler;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);

            // Handle market URLs
            // https://play.google.com/store/apps/details?id=com.example.foobar
            if ("market".equals(uri.getScheme())
                    || "play.google.com".equals(uri.getHost())) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace(
                        "https://play.google.com/store/apps", "market:/")));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                WonderPush.getApplicationContext().startActivity(intent);
                return true;
            }
            // Handle web callback
            if (mWebCallbackHandler.handleWebCallback(uri)) {
                return true;
            }

            return false;
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WonderPush.logDebug("loading url: " + url);
            // OnPageStarted is called twice on error
            if (!mIsLoading) {
                mError = false;
                setUserInterfaceState(new LoadingState());
            }
            if (null != mWebviewTimer) {
                mWebviewTimer.cancel();
                mWebviewTimer = null;
            }
            mMainThreadHandler = new Handler();
            mWebviewTimer = new Timer("webviewTimeout", true);
            mWebviewTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mMainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view.stopLoading();
                            mMessageView.setText(R.string.wonderpush_network_error);
                            setUserInterfaceState(new ErrorState());
                            // if we are preloading we must remove the preloaded view from the preloaded view pool
                            // if (WonderPushView.this.mIsPreloading) {
                            //     WonderPush.freePreloadedResources(WonderPushView.this.mInitialResource);
                            // }
                        }
                    });
                }
            }, WonderPush.WEBVIEW_REQUEST_TOTAL_TIMEOUT);

            mIsLoading = true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            if (null != failingUrl && failingUrl.equals(view.getUrl())) {
                mError = true;
                if (null != mMessageView)
                    mMessageView.setText(description);
                setUserInterfaceState(new ErrorState());
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mWebviewTimer != null) {
                mWebviewTimer.cancel();
            }
            mMainThreadHandler = null;
            mWebviewTimer = null;
            mIsLoading = false;
            if (mIsPreloading) {
                Uri parsedUri = Uri.parse(url);
                String resource = WonderPushUriHelper.getResource(parsedUri);
                Intent intent = new Intent(WonderPush.INTENT_RESOURCE_PRELOADED);
                intent.putExtra(WonderPush.INTENT_RESOURCE_PRELOADED_EXTRA_PATH, resource);

                LocalBroadcastManager.getInstance(WonderPush.getApplicationContext()).sendBroadcast(intent);
            }
            if (null != url && url.equals(view.getUrl())) {
                if (!mError) {
                    setUserInterfaceState(new WebContentState());
                }
            }
        }

    }

    /**
     * A class that optionally handles a web callback uri. The callback uri should
     * be "/web/callback" and provide a code and a status. Depending on these 2
     * parameters, this object will trigger various behaviors.
     */
    private class WonderPushWebCallbackHandler {

        private static final String sCallbackResource = "/web/callback";

        protected boolean handleWebCallback(Uri uri) {
            // Only handle when uri is the callback resource
            if (!sCallbackResource.equals(WonderPushUriHelper.getResource(uri))) {
                return false;
            }

            // Parse status and code
            String statusString = uri.getQueryParameter("status");
            String codeString = uri.getQueryParameter("code");
            int status, code;
            try {
                status = Integer.parseInt(statusString);
                code = Integer.parseInt(codeString);
            } catch (NumberFormatException e) {
                Log.e(TAG, String.format("Invalid status or code (should be an int): %s %s",
                        statusString, codeString), e);
                return false;
            }

            // Successes
            if (300 > status) {
                if (handleCloseWebView(uri, status, code)) {
                    return true;
                }
            } else {
                // 404 errors: let the user see them
                if (404 == status) {
                    return false;
                }
                // Generic errors
                if (handleGenericError(uri, status, code)) {
                    return true;
                }
                // Invalid SID
                if (handleInvalidSIDError(uri, status, code)) {
                    return true;
                }
                // HTTPS required
            }

            return false;
        }

        private boolean handleInvalidSIDError(Uri uri, int status, int code) {
            if (WonderPush.ERROR_INVALID_SID != code) {
                return false;
            }

            // Invalidate access token
            WonderPushRestClient.setAccessToken(null);

            // Invalidate SID
            WonderPushRestClient.setSID(null);

            // Request a new anonymous access token
            WonderPushRestClient.fetchAnonymousAccessTokenIfNeeded();

            return true;
        }

        private boolean handleGenericError(Uri uri, int status, int code) {
            return true;
        }

        private boolean handleCloseWebView(Uri uri, int status, int code) {
            close();
            return true;
        }

    }

    private abstract class UserInterfaceState {

        public abstract void onEnterState();

        public void onLeaveState() {
        }

    }

    private class InitialState extends UserInterfaceState {

        @Override
        public void onEnterState() {
            mProgressBar.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mCloseButton.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.GONE);
        }

        @Override
        public void onLeaveState() {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    private class LoadingState extends UserInterfaceState {

        @Override
        public void onEnterState() {
            mProgressBar.setVisibility(View.VISIBLE);
            if (mStateListener != null) {
                mStateListener.onLoading();
            }
        }

        @Override
        public void onLeaveState() {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    public static interface OnStateListener {
        public void onLoading();
        public void onLoaded();
        public void onError();
        public void onClose();
    }

    private class WebContentState extends UserInterfaceState {

        @Override
        public void onEnterState() {
            mWebView.setVisibility(View.VISIBLE);
            if (mUseCloseButton) {
                mCloseButton.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.GONE);

            mCloseButton.bringToFront();
            if (mStateListener != null) {
                mStateListener.onLoaded();
            }
        }
    }

    private class ErrorState extends UserInterfaceState {

        @Override
        public void onEnterState() {
            mWebView.setVisibility(View.GONE);
            mCloseButton.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
            if (!WonderPushView.this.mIsPreloading) {
                Toast.makeText(getContext(), R.string.wonderpush_network_error, Toast.LENGTH_LONG).show();
            }
            if (mStateListener != null) {
                mStateListener.onError();
            }
        }

        @Override
        public void onLeaveState() {
            mErrorLayout.setVisibility(View.GONE);
        }

    }

}
