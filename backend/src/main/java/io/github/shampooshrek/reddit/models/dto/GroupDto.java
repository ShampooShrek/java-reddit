package io.github.shampooshrek.reddit.models.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.GroupBanner;
import io.github.shampooshrek.reddit.models.GroupImage;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.User;

public class GroupDto {
  private long id;
  private String groupName;
  private String description;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<UserDto> users;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<UserDto> admins;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<PostDto> posts;
  private GroupImage groupImage;
  private GroupBanner groupBanner;

  public GroupDto() {
  }

  public static GroupDto FromEntity(Group group) {
    GroupDto groupDto = new GroupDto();
    groupDto.setId(group.getId());
    groupDto.setGroupName(group.getGroupName());
    groupDto.setDescription(group.getDescription());
    groupDto.setGroupImage(group.getGroupImage());
    groupDto.setGroupBanner(group.getGroupBanner());
    return groupDto;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<UserDto> getUsers() {
    return users;
  }

  public GroupDto setUsers(List<User> users) {
    List<UserDto> usersDto = users.stream().map(e -> UserDto.FromEntity(e)).toList();
    this.users = usersDto;
    return this;
  }

  public GroupDto setUsersDto(List<UserDto> users) {
    this.users = users;
    return this;
  }

  public List<UserDto> getAdmins() {
    return admins;
  }

  public GroupDto setAdmins(List<User> admins) {
    List<UserDto> usersDto = admins.stream().map(e -> UserDto.FromEntity(e)).toList();
    this.admins = usersDto;
    return this;
  }

  public GroupDto SetAdminsDto(List<UserDto> admins) {
    this.admins = admins;
    return this;
  }

  public List<PostDto> getPosts() {
    return posts;
  }

  public GroupDto setPosts(List<Post> posts) {
    List<PostDto> postsDto = posts.stream().map(p -> PostDto.FromEntity(p)).toList();
    this.posts = postsDto;
    return this;
  }

  public GroupDto setPostsDto(List<PostDto> posts) {
    this.posts = posts;
    return this;
  }

  public GroupImage getGroupImage() {
    return groupImage;
  }

  public GroupDto setGroupImage(GroupImage groupImage) {
    this.groupImage = groupImage;
    return this;
  }

  public GroupBanner getGroupBanner() {
    return groupBanner;
  }

  public GroupDto setGroupBanner(GroupBanner groupBanner) {
    this.groupBanner = groupBanner;
    return this;
  }

}
