package io.github.shampooshrek.reddit.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Transient
  // @JsonIgnore
  private String confPassword;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  private List<Post> posts = new ArrayList<>();

  @OneToOne(mappedBy = "user")
  @JoinColumn(name = "user_image_id")
  private UserImage userImage;

  @OneToOne(mappedBy = "user")
  @JoinColumn(name = "refresh_token_id")
  private RefreshToken refreshToken;

  @ManyToMany
  @JoinTable(name = "user_groups", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Group> groups = new ArrayList<>();

  @OneToMany(mappedBy = "userCreator")
  private List<Group> creatorGroups = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "admin_groups", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Group> adminGroups = new ArrayList<>();

  public User() {
  }

  public User(String name, String nickname, String email, String password) {
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public String getConfPassword() {
    return confPassword;
  }

  public void setConfPassword(String confPassword) {
    this.confPassword = confPassword;
  }

  public UserImage getUserImage() {
    return userImage;
  }

  public void setUserImage(UserImage userImage) {
    this.userImage = userImage;
  }

  public List<Group> getGroups() {
    if (groups == null) {
      groups = new ArrayList<>();
    }
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public List<Group> getAdminGroups() {
    if (adminGroups == null) {
      adminGroups = new ArrayList<>();
    }
    return adminGroups;
  }

  public void setAdminGroups(List<Group> adminGroups) {
    this.adminGroups = adminGroups;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public List<Group> getCreatorGroups() {
    if (creatorGroups == null) {
      creatorGroups = new ArrayList<>();
    }
    return creatorGroups;
  }

  public void setCreatorGroups(List<Group> creatorGrougs) {
    this.creatorGroups = creatorGrougs;
  }

  public RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }
}
