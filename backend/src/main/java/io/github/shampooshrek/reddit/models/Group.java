package io.github.shampooshrek.reddit.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_groups")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "group_name", nullable = false)
  private String groupName;

  private String description;

  private boolean deleted = false;

  @ManyToMany(mappedBy = "groups")
  private List<User> users = new ArrayList<>();

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User userCreator;

  @ManyToMany(mappedBy = "adminGroups")
  private List<User> admins = new ArrayList<>();

  @OneToMany(mappedBy = "group")
  private List<Post> posts = new ArrayList<>();

  @OneToOne(mappedBy = "group")
  @JoinColumn(name = "group_image_id")
  private GroupImage groupImage;

  @OneToOne(mappedBy = "group")
  @JoinColumn(name = "group_banner_id")
  private GroupBanner groupBanner;

  public Group() {
  }

  public Group(String groupName, String description, List<User> users, List<Post> posts, GroupImage groupImage,
      GroupBanner groupBanner) {
    this.groupName = groupName;
    this.description = description;
    this.users = users;
    this.posts = posts;
    this.groupImage = groupImage;
    this.groupBanner = groupBanner;
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

  public List<User> getUsers() {
    if (users == null) {
      users = new ArrayList<>();
    }
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public List<Post> getActivedPosts() {
    return posts.stream()
        .filter(p -> !p.isDeleted())
        .toList();
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public GroupImage getGroupImage() {
    return groupImage;
  }

  public void setGroupImage(GroupImage groupImage) {
    this.groupImage = groupImage;
  }

  public GroupBanner getGroupBanner() {
    return groupBanner;
  }

  public void setBannerImage(GroupBanner groupBanner) {
    this.groupBanner = groupBanner;
  }

  public List<User> getAdmins() {
    if (admins == null) {
      admins = new ArrayList<>();
    }
    return admins;
  }

  public void setAdmins(List<User> admins) {
    this.admins = admins;
  }

  public User getUserCreator() {
    return userCreator;
  }

  public void setUserCreator(User userCreator) {
    this.userCreator = userCreator;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

}
