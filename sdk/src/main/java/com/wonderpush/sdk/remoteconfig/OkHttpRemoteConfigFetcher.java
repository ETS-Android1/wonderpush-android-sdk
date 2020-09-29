package com.wonderpush.sdk.remoteconfig;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class OkHttpRemoteConfigFetcher implements RemoteConfigFetcher {
    @Nonnull
    OkHttpClient client = new OkHttpClient();
    @Nonnull
    String clientId;
    @Nonnull
    final private SafeDeferProvider safeDeferProvider;
    public OkHttpRemoteConfigFetcher(String clientId, SafeDeferProvider safeDeferProvider) {
        this.clientId = clientId;
        this.safeDeferProvider = safeDeferProvider;
    }

    public interface SafeDeferProvider {
        void safeDefer(Runnable r, long defer);
    }

    @Override
    public void fetchRemoteConfig(@Nullable String version, @Nonnull RemoteConfigHandler handler) {
        String url = String.format(Locale.ENGLISH, "%s%s%s?_=%d",Constants.REMOTE_CONFIG_BASE_URL, clientId, Constants.REMOTE_CONFIG_SUFFIX, new Date().getTime());
        this.safeDeferProvider.safeDefer(() -> {
            Request request = new Request.Builder().url(url).get().build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.handle(null, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        handler.handle(null, new Exception("Invalid status code from remote config server:" + response.code()));
                        return;
                    }

                    JSONObject responseJson;
                    try {
                        responseJson = new JSONObject(response.body().string());
                        String version = responseJson.optString("version", Long.toString(responseJson.optLong("version", 0)));
                        if (version != null) {
                            Long maxAge = responseJson.optLong("maxAge", responseJson.optLong("cacheTtl", 0));
                            Long minAge = responseJson.optLong("minAge", responseJson.optLong("cacheMinAge", 0));
                            RemoteConfig config = RemoteConfig.with(responseJson, version, DateHelper.now(), maxAge, minAge);
                            handler.handle(config, null);
                            return;
                        }
                        handler.handle(null, new Exception("Invalid remote config format"));
                    } catch (JSONException e) {
                        handler.handle(null, e);
                        return;
                    }
                }
            });
        }, 0);
    }
}
