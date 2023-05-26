package com.example.bankingbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.Loans;


public interface LoanRepository extends JpaRepository<Loans, Long>{
	
	Loans findByCardNo(Long cardNo);

}
