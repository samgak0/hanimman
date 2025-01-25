package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    // 주소검색
    List<Address> findByNeighborhoodNameContaining(String neighborhoodName);

    Optional<Address> findById(@NonNull String id);
}