package com.example.bankingbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bankingbackend.Entity.Accounts;


@Repository
public interface AccountsRepository extends JpaRepository<Accounts,Long>{

	List<Accounts> findByCustomerId(String customerId);
	Accounts findByAccountNo(String accountNo);
//	static Optional<Accounts> findByCustomerId(String customerId) {
//		// TODO Auto-generated method stub
//		
//	}
}

