// package io.github.shampooshrek.firstcrud.repositories;

// import java.util.Optional;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.ActiveProfiles;

// import io.github.shampooshrek.firstcrud.models.User;
// import jakarta.persistence.EntityManager;
// import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
// @ActiveProfiles("test")
// public class UserRepositoryTest {

//   @Autowired
//   UserRepository userRepository;

//   @Autowired
//   EntityManager entityManager;

//   @Test
//   @DisplayName("Should return User successfully from DB")
//   void findUserByNickname() {
//     User user = new User("aoopa", "champs", "adad@adad", "asdasd", "asdasd");
//     this.createUser(user);
//     Optional<User> foundedUser = this.userRepository.findByEmailOrNickname(user.getEmail());
//     assertThat(foundedUser.isPresent()).isTrue();
//   }

//   private User createUser(User user) {
//     User u = user;
//     this.entityManager.persist(u);
//     return u;
//   }
// }
