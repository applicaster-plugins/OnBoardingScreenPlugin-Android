package com.applicaster.onboarding.screen.mapper;

import com.applicaster.onboarding.screen.model.PluginConfig;
import com.applicaster.util.PreferenceUtil;
import com.applicaster.util.StringUtil;

import java.util.Map;

public class PluginDataMapper {

    public PluginConfig mapParamsToConfig(Map params) {
        String onBoardingFeedPath = getParam(params, "onBoardingFeedPath", "");
        String backgroundColor = getParam(params, "backgroundColor", "#000000");
        String highlightColor = getParam(params, "highlightColor", "#DD4400");
        String titleColor = getParam(params, "titleColor", "#FFFFFF");
        String categoryBackgroundColor = getParam(params, "categoryBackgroundColor", "#000000");
        boolean applyBorder = StringUtil.booleanValue(getParam(params, "applyBorder", "false"));
        return new PluginConfig(onBoardingFeedPath, backgroundColor, highlightColor, titleColor, categoryBackgroundColor, applyBorder);
    }

    private <T> T getParam(Map params, String key, T defaultValue) {
        Object param = params.get(key);
        return param == null ? defaultValue : (T) param;
    }
}
