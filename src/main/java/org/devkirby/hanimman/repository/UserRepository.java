package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Gender;
import org.devkirby.hanimman.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNameAndPhonenumAndGenderAndBirth(String name, String phonenum, Gender gender, LocalDate birth);
    boolean existsByCodenum(String codenum);
    Optional<User> findByCodenum(String codenum);
    Optional<User> findById(Integer id);
}
