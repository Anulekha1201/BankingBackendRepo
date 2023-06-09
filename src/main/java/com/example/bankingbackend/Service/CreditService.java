package com.example.bankingbackend.Service;

import com.example.bankingbackend.Entity.Credit;
import com.example.bankingbackend.repository.CreditRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    @Autowired
    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public boolean checkAccountExists(Long accountNo) {
        Credit credit = creditRepository.findByAccountNo(accountNo);
        return credit != null;
    }

    public List<Credit> getcredit(){
    	return creditRepository.findAll();
    }

}