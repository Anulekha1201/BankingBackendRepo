package com.example.bankingbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankingbackend.Entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long>{

	Optional<UserInfo> findByCustomerId(String customeridref);

	

}
