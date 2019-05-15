package com.applicaster.onboarding.screen;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.applicaster.onboarding.screen.interactor.SavePluginConfig;
import com.applicaster.onboarding.screen.mapper.PluginDataMapper;
import com.applicaster.plugin_manager.PluginSchemeI;
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI;
import com.applicaster.plugin_manager.hook.HookListener;
import com.applicaster.session.SessionStorage;
import com.applicaster.util.PreferenceUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OnboardingScreenContract implements PluginSchemeI, ApplicationLoaderHookUpI {

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
        String[] selections = PreferenceUtil.getInstance().getStringArrayPref("user_ob_selections", null);

        if (selections == null) {
            navigator.goToOnboardingScreen(context, listener, Collections.<String>emptyList());
        } else {
            List<String> previousOBSelections = Arrays.asList(selections);
            SessionStorage.INSTANCE.set("user_content_preferences", previousOBSelections.toString());
            listener.onHookFinished();
        }
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
            String[] selections = PreferenceUtil.getInstance().getStringArrayPref("user_ob_selections", null);
            List<String> previousOBSelections = Collections.emptyList();
            if (selections != null) {
                previousOBSelections = Arrays.asList(selections);
                SessionStorage.INSTANCE.set("user_content_preferences", previousOBSelections.toString());
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
            if ("content_preferences".equals(data.get("action"))) {
                verified = true;
            }
        }

        return verified;
    }
}