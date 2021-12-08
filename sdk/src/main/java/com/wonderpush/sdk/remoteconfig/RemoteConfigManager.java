package com.wonderpush.sdk.remoteconfig;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import com.wonderpush.sdk.SimpleVersion;
import com.wonderpush.sdk.WonderPush;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteConfigManager {
    private static final String TAG = "WonderPush." + RemoteConfigManager.class.getSimpleName();
    @Nonnull
    public RemoteConfigFetcher remoteConfigFetcher;

    @Nonnull
    public RemoteConfigStorage remoteConfigStorage;

    @Nullable
    public Context applicationContext;

    public long minimumFetchInterval;
    public long minimumConfigAge;
    public long maximumConfigAge;

    boolean isFetching;
    @Nullable
    Date lastFetchDate;
    @Nullable
    RemoteConfig storedConfig;
    @Nullable
    String storedHighestVersion;
    @Nonnull
    List<RemoteConfigHandler> queuedHandlers = new ArrayList<>();

    public RemoteConfigManager(@Nonnull RemoteConfigFetcher fetcher, @Nonnull RemoteConfigStorage storage) {
        this(fetcher, storage, null);
    }
    public RemoteConfigManager(@Nonnull RemoteConfigFetcher fetcher, @Nonnull RemoteConfigStorage storage, @Nullable Context context) {
        this.remoteConfigFetcher = fetcher;
        this.remoteConfigStorage = storage;
        this.applicationContext = context != null ? context.getApplicationContext() : null;
        this.minimumFetchInterval = Constants.REMOTE_CONFIG_DEFAULT_MINIMUM_CONFIG_AGE;
        this.minimumConfigAge = Constants.REMOTE_CONFIG_DEFAULT_MINIMUM_CONFIG_AGE;
        this.maximumConfigAge = Constants.REMOTE_CONFIG_DEFAULT_MAXIMUM_CONFIG_AGE;
    }

    public void declareVersion(@Nonnull String version) {
        if (!new SimpleVersion(version).isValid()) return;
        remoteConfigStorage.declareVersion(version, (Throwable declareVersionError) -> {
            if (declareVersionError != null) {
                Log.e(TAG, "Error declaring version to storage", declareVersionError);
            } else {
                synchronized (this) {
                    if (storedHighestVersion == null
                            || RemoteConfig.compareVersions(storedHighestVersion, version) < 0) {
                        storedHighestVersion = version;
                    }
                }
            }
            readConfigAndHighestDeclaredVersionFromStorage((RemoteConfig config, String highestVersion, Throwable readError) -> {
                if (readError != null) {
                    Log.e(TAG, "Could not get RemoteConfig from storage", readError);
                    return;
                }

                Date now = DateHelper.now();
                if (config != null) {
                    // Do not fetch too often
                    long configAge = now.getTime() - config.getFetchDate().getTime();
                    if (configAge < minimumConfigAge || !config.hasReachedMinAge()) return;

                    // If we're declaring the same version as the current config, update the current config's fetchDate
                    if (RemoteConfig.compareVersions(config.getVersion(), version) == 0) {
                        RemoteConfig configWithUpdatedDate = RemoteConfig.with(config.getData(), config.getVersion(), now, config.getMaxAge(), config.getMinAge());
                        remoteConfigStorage.storeRemoteConfig(configWithUpdatedDate, (Throwable storageError) -> {
                            synchronized (this) {
                                if (storageError == null) storedConfig = configWithUpdatedDate;
                            }
                        });
                        return;
                    }

                    // Only fetch a higher version
                    if (RemoteConfig.compareVersions(config.getVersion(), highestVersion) >= 0) return;
                }

                // Do not update too frequently
                if (lastFetchDate != null) {
                    long lastFetchInterval = now.getTime() - lastFetchDate.getTime();
                    if (lastFetchInterval < minimumFetchInterval) return;
                }

                fetchAndStoreConfig(highestVersion, config, null);
            });
        });
    }

    public void read(@Nonnull RemoteConfigHandler handler) {
        synchronized (this) {
            if (isFetching) {
                queuedHandlers.add(handler);
                return;
            }
        }

        readConfigAndHighestDeclaredVersionFromStorage((RemoteConfig storedConfig, String highestVersion, Throwable storageError) -> {
            if (storageError != null) {
                handler.handle(null, storageError);
                return;
            }

            Date now = DateHelper.now();
            long lastFetchInterval = lastFetchDate != null ? now.getTime() - lastFetchDate.getTime() : Long.MAX_VALUE;

            if (storedConfig == null) {
                // Do not update too frequently
                if (lastFetchDate != null && lastFetchInterval < minimumFetchInterval) {
                    handler.handle(null, null);
                    return;
                }

                fetchAndStoreConfig(null, null, (RemoteConfig fetchedConfig, Throwable fetchError) -> {
                    if (fetchError != null) {
                        handler.handle(null, fetchError);
                        return;
                    }
                    handler.handle(fetchedConfig, null);
                });
                return;
            }

            boolean higherVersionExists = RemoteConfig.compareVersions(storedConfig.getVersion(), highestVersion) < 0;
            boolean shouldFetch = higherVersionExists;
            String versionToFetch = highestVersion;

            // Do not fetch too often
            long configAge = now.getTime() - storedConfig.getFetchDate().getTime();
            if (shouldFetch && (configAge < minimumConfigAge || !storedConfig.hasReachedMinAge())) shouldFetch = false;

            // Force fetch if expired
            boolean isExpired = configAge > maximumConfigAge || storedConfig.isExpired();
            if (!shouldFetch && isExpired) {
                shouldFetch = true;
                if (!higherVersionExists) versionToFetch = storedConfig.getVersion();
            }

            // Do not fetch too often
            if (shouldFetch && lastFetchDate != null && lastFetchInterval < minimumFetchInterval) shouldFetch = false;

            if (!shouldFetch) {
                handler.handle(storedConfig, null);
                return;
            }

            fetchAndStoreConfig(versionToFetch, storedConfig, (RemoteConfig fetchedConfig, Throwable fetchError) -> {
                handler.handle(fetchedConfig != null ? fetchedConfig : storedConfig, fetchError);
            });
        });
    }

    private void readConfigAndHighestDeclaredVersionFromStorage(RemoteConfigVersionHandler handler) {
        if (storedConfig != null && storedHighestVersion != null) {
            handler.handle(storedConfig, storedHighestVersion, null);
        } else {
            remoteConfigStorage.loadRemoteConfigAndHighestDeclaredVersion((RemoteConfig config, String highestVersion, Throwable error) -> {
                if (error == null) {
                    synchronized (this) {
                        if (config != null) storedConfig = config;
                        if (highestVersion != null) storedHighestVersion = highestVersion;
                    }
                }

                handler.handle(config, highestVersion, error);
            });
        }

    }

    private void fetchAndStoreConfig(@Nullable String version, @Nullable RemoteConfig currentConfig, @Nullable RemoteConfigHandler completion) {
        synchronized (this) {
            if (isFetching) {
                if (completion != null) {
                    queuedHandlers.add(completion);
                }
                return;
            }
        }

        // Is fetch disabled?
        if (currentConfig != null && currentConfig.getData() != null && currentConfig.getData().optBoolean(Constants.DISABLE_FETCH_KEY, false)) {
            if (completion != null) completion.handle(currentConfig, null);
            return;
        }

        synchronized (this) {
            lastFetchDate = DateHelper.now();
            isFetching = true;
        }
        remoteConfigFetcher.fetchRemoteConfig(version, (RemoteConfig newConfig, Throwable fetchError) -> {

            RemoteConfigHandler handler = (RemoteConfig config, Throwable error) -> {
                List<RemoteConfigHandler> queuedHandlersCopy;
                synchronized (this) {
                    isFetching = false;
                    queuedHandlersCopy = new ArrayList<>(queuedHandlers);
                    queuedHandlers.clear();
                }
                for (RemoteConfigHandler h : queuedHandlersCopy) {
                    h.handle(config, error);
                }
                if (completion != null) completion.handle(config, error);
            };

            if (newConfig != null && fetchError == null) {
                if (currentConfig != null && currentConfig.hasHigherVersionThan(newConfig)) {
                    handler.handle(currentConfig, null);
                    return;
                }

                if (WonderPush.getLogging()) Log.d(TAG, "Got new configuration with version " + newConfig.getVersion());
                remoteConfigStorage.storeRemoteConfig(newConfig, (Throwable storageError) -> {
                    if (storageError != null) {
                        Log.e(TAG, "Could not store RemoteConfig in storage", storageError);
                        handler.handle(null, storageError);
                    } else {
                        synchronized (this) {
                            storedConfig = newConfig;
                        }
                        remoteConfigStorage.declareVersion(newConfig.getVersion(), (Throwable declareError) -> {
                            if (declareError != null) {
                                Log.e(TAG, "Error declaring version to storage", declareError);
                            }
                            if (currentConfig == null || newConfig.hasHigherVersionThan(currentConfig)) {
                                LocalBroadcastManager mgr = applicationContext != null ? LocalBroadcastManager.getInstance(applicationContext) : null;
                                if (mgr != null) {
                                    Intent intent = new Intent(Constants.INTENT_REMOTE_CONFIG_UPDATED);
                                    intent.putExtra(Constants.EXTRA_REMOTE_CONFIG, newConfig.toString());
                                    mgr.sendBroadcast(intent);
                                }
                            }
                            handler.handle(newConfig, null);
                        });
                    }
                });
                return;
            }
            if (fetchError != null) {
                Log.e(TAG, "Could not fetch RemoteConfig from server", fetchError);
            }
            handler.handle(currentConfig, fetchError);
        });
    }

}
