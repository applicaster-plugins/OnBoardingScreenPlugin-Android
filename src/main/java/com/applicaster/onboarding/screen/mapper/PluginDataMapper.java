package com.applicaster.onboarding.screen.mapper;

import com.applicaster.onboarding.screen.model.PluginConfig;

import java.util.Map;

public class PluginDataMapper {

  public PluginConfig mapParamsToConfig(Map params) {
    String logo = (String) params.get("logo");
    String title = (String) params.get("title");
    String titleTextColor = (String) params.get("title_text_color");
    String sorting = (String) params.get("library_list_ordering");
    return new PluginConfig(logo, title, titleTextColor, PluginConfig.Sorting.getEnum(sorting));
  }
}
