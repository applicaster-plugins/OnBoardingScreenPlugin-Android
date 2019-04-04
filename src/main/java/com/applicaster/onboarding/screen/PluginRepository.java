package com.applicaster.onboarding.screen;

import com.applicaster.onboarding.screen.model.PluginConfig;

public interface PluginRepository {

    public boolean setPluginConfiguration(PluginConfig pluginConfig);

    public PluginConfig getPluginConfig();

    public void setIsPluginAlreadyDisplayed();

    public boolean isPluginAlreadyDisplayed();

}
