package com.applicaster.onboarding.screen;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.applicaster.onboarding.screen.interactor.SavePluginConfig;
import com.applicaster.onboarding.screen.mapper.PluginDataMapper;
import com.applicaster.plugin_manager.PluginSchemeI;
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI;
import com.applicaster.plugin_manager.hook.HookListener;
import com.applicaster.storage.LocalStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OnboardingScreenContract implements PluginSchemeI, ApplicationLoaderHookUpI {

    public static final String USER_RECOMMENDATION_KEY = "userRecommendationTags";
    public static final String USER_RECOMMENDATION_NAMESPACE = "onboarding";

    private final SavePluginConfig savePluginConfig;
    private final PluginRepository pluginRepository;
    private final Navigator navigator;

    public OnboardingScreenContract() {
        pluginRepository = PluginDataRepository.INSTANCE;
        navigator = new Navigator();
        savePluginConfig = new SavePluginConfig(pluginRepository, new PluginDataMapper());
    }

    @Override
    public void setPluginConfigurationParams(Map params) {
        savePluginConfig.execute(SavePluginConfig.Params.forPluginParams(params));
    }

    @Override
    public void executeOnApplicationReady(final Context context, final HookListener listener) {
        String[] selections = getStoredSelections();
        if (selections == null) {
            navigator.goToOnboardingScreen(context, listener, Collections.<String>emptyList());
        } else {
            List<String> previousOBSelections = Arrays.asList(selections);
            listener.onHookFinished();
        }
    }

    private String[] getStoredSelections() {
        String storedData = LocalStorage.INSTANCE.getStorageRepository().get(USER_RECOMMENDATION_KEY, USER_RECOMMENDATION_NAMESPACE);
        return storedData == null ? null : storedData.split(", ");
    }

    @Override
    public void executeOnStartup(Context context, HookListener listener) {
        // do nothing
        listener.onHookFinished();
    }

    @Override
    public boolean handlePluginScheme(Context context, Map<String, String> data) {
        boolean wasHandled = false;
        if (verifiedPluginSchema(data)) {
            String[] selections = getStoredSelections();
            List<String> previousOBSelections = Collections.emptyList();
            if (selections != null) {
                previousOBSelections = Arrays.asList(selections);
            }
            navigator.goToOnboardingScreen(context, null, previousOBSelections);
            wasHandled = true;
        }
        return wasHandled;
    }

    @VisibleForTesting
    private boolean verifiedPluginSchema(Map<String, String> data) {
        boolean verified = false;
        if ("general".equals(data.get("type"))) {
            if ("ob_preferences".equals(data.get("action"))) {
                verified = true;
            }
        }

        return verified;
    }
}