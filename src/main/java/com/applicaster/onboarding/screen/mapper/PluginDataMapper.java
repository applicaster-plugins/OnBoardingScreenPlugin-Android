package com.applicaster.onboarding.screen.mapper;

import com.applicaster.onboarding.screen.model.PluginConfig;
import com.applicaster.util.PreferenceUtil;
import com.applicaster.util.StringUtil;

import java.util.Map;

public class PluginDataMapper {

    public PluginConfig mapParamsToConfig(Map params) {
        String onBoardingFeedPath = (String) params.get("onBoardingFeedPath");
        String backgroundColor = (String) params.get("backgroundColor");
        String highlightColor = (String) params.get("highlightColor");
        String titleColor = (String) params.get("titleColor");
        String categoryBackgroundColor = (String) params.get("categoryBackgroundColor");
        boolean applyBorder = StringUtil.booleanValue(params.get("applyBorder").toString());
        return new PluginConfig(onBoardingFeedPath, backgroundColor, highlightColor, titleColor, categoryBackgroundColor, applyBorder);
    }
}
