package io.github.shampooshrek.reddit.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.shampooshrek.reddit.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("SELECT u, p FROM User u LEFT JOIN u.posts p")
  public List<User> findAllWithPosts();

  @Query("SELECT u FROM User u WHERE u.email = :value")
  public Optional<User> findByEmailOrNickname(@Param("value") String value);
}
