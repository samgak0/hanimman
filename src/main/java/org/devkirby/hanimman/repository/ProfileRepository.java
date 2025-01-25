package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Profile;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByParent(User parent);
}
