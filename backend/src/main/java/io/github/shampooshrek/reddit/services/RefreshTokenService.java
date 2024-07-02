package io.github.shampooshrek.reddit.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.shampooshrek.reddit.models.RefreshToken;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.repositories.RefreshTokenRepository;
import io.github.shampooshrek.reddit.repositories.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${security.jwt.refresh-token-time}")
  private long refreshTokenExpiration;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(long userId) {
    User user = userRepository.findById(userId).get();
    RefreshToken refreshToken;
    if (user.getRefreshToken() != null) {
      refreshToken = user.getRefreshToken();
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
    } else {
      refreshToken = new RefreshToken();
      refreshToken.setUser(user);
      refreshToken.setToken(UUID.randomUUID().toString());
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
    }
    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RuntimeException("Refresh token expired. Please login again");
    }
    return token;
  }

  public void deleteByUserId(long userId) {
    User user = userRepository.findById(userId).get();
    refreshTokenRepository.deleteByUser(user);
  }
}
