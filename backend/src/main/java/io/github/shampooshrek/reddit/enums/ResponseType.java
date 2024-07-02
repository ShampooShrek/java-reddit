package io.github.shampooshrek.reddit.enums;

public enum ResponseType {
  SUCCESS("success"),
  ERROR("error");

  public final String label;

  private ResponseType(String label) {
    this.label = label;
  }
}
