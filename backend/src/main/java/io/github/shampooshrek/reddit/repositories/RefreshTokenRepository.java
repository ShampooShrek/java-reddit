package io.github.shampooshrek.reddit.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.shampooshrek.reddit.models.RefreshToken;
import io.github.shampooshrek.reddit.models.User;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String Token);

  void deleteByUser(User user);
}
