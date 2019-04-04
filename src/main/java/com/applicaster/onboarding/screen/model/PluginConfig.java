package com.applicaster.onboarding.screen.model;

public class PluginConfig {

  private String logo;
  private String title;
  private String titleTextColor;
  private Sorting sorting;

  public PluginConfig(String logo, String title, String titleTextColor, Sorting sorting) {
    this.logo = logo;
    this.title = title;
    this.titleTextColor = titleTextColor;
    this.sorting = sorting;
  }

  public String getLogo() {
    return logo;
  }

  public String getTitle() {
    return title;
  }

  public String getTitleTextColor() {
    return titleTextColor;
  }

  public Sorting getSorting() {
    return sorting;
  }

  public enum Sorting {
    NAME_ASCENDING("Name - Ascending"), NAME_DESCENDING("Name - Descending"), OFF("Off");

    private final String sorting;

    Sorting(String sorting) {
      this.sorting = sorting;
    }

    public static Sorting getEnum(String value) {
      for (Sorting v : values())
        if (v.getValue().equalsIgnoreCase(value)) return v;
      throw new IllegalArgumentException();
    }

    public String getValue() {
      return sorting;
    }

    @Override public String toString() {
      return getValue();
    }
  }
}
