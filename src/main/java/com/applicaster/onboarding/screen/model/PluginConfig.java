package com.applicaster.onboarding.screen.model;

public class PluginConfig {

    String onBoardingFeedPath;
    String backgroundColor;
    String highlightColor;
    String titleColor;
    String categoryBackgroundColor;
    boolean applyBorder;

    public PluginConfig(String onBoardingFeedPath, String backgroundColor, String highlightColor, String titleColor, String categoryBackgroundColor, boolean applyBorder) {
        this.onBoardingFeedPath = onBoardingFeedPath;
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
        this.titleColor = titleColor;
        this.categoryBackgroundColor = categoryBackgroundColor;
        this.applyBorder = applyBorder;
    }

    public String getOnBoardingFeedPath() {
        return onBoardingFeedPath;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getHighlightColor() {
        return highlightColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public String getCategoryBackgroundColor() {
        return categoryBackgroundColor;
    }

    public boolean isApplyBorder() {
        return applyBorder;
    }
}
