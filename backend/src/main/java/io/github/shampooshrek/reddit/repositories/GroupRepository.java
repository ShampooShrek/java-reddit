package io.github.shampooshrek.reddit.repositories;

import org.springframework.data.repository.CrudRepository;

import io.github.shampooshrek.reddit.models.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
