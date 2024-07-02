package io.github.shampooshrek.reddit.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_image")
public class GroupBanner extends GroupImageTemplate {

  public GroupBanner() {
  }

  public GroupBanner(String name, String originalName, long fileSize, String fileUrl) {
    super(name, originalName, fileSize, fileUrl);
  }

  public GroupBanner(String name, String originalName, long fileSize, String fileUrl, Group group) {
    super(name, originalName, fileSize, fileUrl, group);
  }

}
