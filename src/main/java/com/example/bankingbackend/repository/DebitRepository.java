package com.example.bankingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankingbackend.Entity.Debit;

public interface DebitRepository extends JpaRepository<Debit, Long> {

}
