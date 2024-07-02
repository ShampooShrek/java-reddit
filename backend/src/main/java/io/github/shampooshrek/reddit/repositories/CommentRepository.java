package io.github.shampooshrek.reddit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.shampooshrek.reddit.models.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
  @Query("SELECT c FROM Comment c WHERE c.parentComment IS NULL AND c.post.id = :postId")
  List<Comment> findCommentsWithoutParent(@Param("postId") long postId);

  @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentId")
  List<Comment> findCommentsPerParent(@Param("parentId") long id);

  // @Query("SELECT new io.github.shampooshrek.firstcrud.models.CommentDto"
  // + "(c.id, c.content, c.createdAt, c.user.id, c.post.id, c.parentComment.id)"
  // + "FROM Comment c WHERE c.parentComment.id = :parentId")
  // List<CommentDto> findCommentsPerParentDto(@Param("parentId") long id);
}
