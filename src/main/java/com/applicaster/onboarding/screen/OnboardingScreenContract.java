package com.applicaster.onboarding.screen;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;

import com.applicaster.atom.model.APAtomError;
import com.applicaster.hook_screen.HookScreen;
import com.applicaster.jspipes.JSManager;
import com.applicaster.onboarding.screen.interactor.SavePluginConfig;
import com.applicaster.onboarding.screen.mapper.PluginDataMapper;
import com.applicaster.plugin_manager.PluginSchemeI;
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI;
import com.applicaster.plugin_manager.hook.HookListener;
import com.applicaster.plugin_manager.screen.PluginScreen;
import com.applicaster.util.OSUtil;
import com.applicaster.util.UrlSchemeUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OnboardingScreenContract implements PluginSchemeI, ApplicationLoaderHookUpI, PluginScreen {

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
//        savePluginConfig.execute(SavePluginConfig.Params.forPluginParams(params));
    }

    @Override
    public void executeOnApplicationReady(final Context context, final HookListener listener) {

        navigator.goToOnboardingScreen(context, listener);

        UrlSchemeUtil.handleInternalUrlScheme(context,"");
//        JSManager.getInstance().get(" ", new JSManager.JSManagerCallback() {
//            @Override
//            public void onResult(Object atom) {
//
//            }
//
//            @Override
//            public void onError(APAtomError error) {
//
//            }
//        });
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
            // TODO - logout
            wasHandled = true;
        }
        return wasHandled;
    }

    @VisibleForTesting
    boolean verifiedPluginSchema(Map<String, String> data) {
        boolean verified = false;
        if ("login".equals(data.get("type"))) {
            if ("logout".equals(data.get("action"))) {
                verified = true;
            }
        }

        return verified;
    }

    @Override
    public void present(Context context, HashMap<String, Object> screenMap, Serializable dataSource, boolean isActivity) {

    }

    @Override
    public Fragment generateFragment(HashMap<String, Object> screenMap, Serializable dataSource) {
        return null;
    }
}