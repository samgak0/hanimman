package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    Optional<UserAddress> findByUserId(Integer userId);
}