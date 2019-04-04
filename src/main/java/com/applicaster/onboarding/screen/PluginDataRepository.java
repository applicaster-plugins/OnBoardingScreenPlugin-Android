package com.applicaster.onboarding.screen;

import com.applicaster.onboarding.screen.model.PluginConfig;
import com.applicaster.util.PreferenceUtil;

public enum PluginDataRepository implements PluginRepository {
    INSTANCE;

    private PluginConfig pluginConfig;

    @Override
    public boolean setPluginConfiguration(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        return true;
    }

    @Override
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    @Override
    public void setIsPluginAlreadyDisplayed() {
        PreferenceUtil.getInstance().setBooleanPref("library_selector_displayed", true);
    }

    @Override
    public boolean isPluginAlreadyDisplayed() {
        return PreferenceUtil.getInstance().getBooleanPref("library_selector_displayed", false);
    }
}
