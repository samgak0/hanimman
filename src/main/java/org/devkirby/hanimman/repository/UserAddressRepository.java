package org.devkirby.hanimman.repository;

import org.devkirby.hanimman.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    //특정 사용자 ID에 해당하는 단일 주소
    UserAddress findByUserId(Integer userId);

    //특정 사용자 ID에 해당하느 모든 주소
    List<UserAddress> findAllByUserId(Integer userId);


}