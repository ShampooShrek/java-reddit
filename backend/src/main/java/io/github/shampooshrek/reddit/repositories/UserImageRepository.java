package io.github.shampooshrek.reddit.repositories;

import io.github.shampooshrek.reddit.models.UserImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends CrudRepository<UserImage, Long> {

}
