package io.github.shampooshrek.reddit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.UserDetailsImpl;
import io.github.shampooshrek.reddit.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository
        .findByEmailOrNickname(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    return UserDetailsImpl.build(user);
  }
}
