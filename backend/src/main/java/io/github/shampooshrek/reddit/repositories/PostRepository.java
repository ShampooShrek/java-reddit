package io.github.shampooshrek.reddit.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.shampooshrek.reddit.models.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
  @Query("SELECT p FROM Post p WHERE p.deleted = false AND p.group.id = :groupId")
  Iterable<Post> findGroupPosts(@Param("groupId") long groupId);
}
