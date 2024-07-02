package io.github.shampooshrek.reddit.models.dto;

import java.util.List;

/**
 * AuthGroupsResponseDto
 */
public class AuthGroupsResponseDto {
  private List<GroupDto> userGroups;
  private List<GroupDto> adminGroups;

  public AuthGroupsResponseDto(List<GroupDto> userGroups, List<GroupDto> adminGroups) {
    this.userGroups = userGroups;
    this.adminGroups = adminGroups;
  }

  public List<GroupDto> getUserGroups() {
    return userGroups;
  }

  public void setUserGroups(List<GroupDto> userGroups) {
    this.userGroups = userGroups;
  }

  public List<GroupDto> getAdminGroups() {
    return adminGroups;
  }

  public void setAdminGroups(List<GroupDto> adminGroups) {
    this.adminGroups = adminGroups;
  }

}
