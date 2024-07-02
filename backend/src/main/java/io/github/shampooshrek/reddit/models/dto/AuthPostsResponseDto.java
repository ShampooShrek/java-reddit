
package io.github.shampooshrek.reddit.models.dto;

import java.util.List;

/**
 * AuthPostsResponseDto
 */
public class AuthPostsResponseDto {
  private List<PostDto> posts;

  public AuthPostsResponseDto(List<PostDto> posts) {
    this.posts = posts;
  }

  public List<PostDto> getPosts() {
    return posts;
  }

  public void setPosts(List<PostDto> posts) {
    this.posts = posts;
  }
}
