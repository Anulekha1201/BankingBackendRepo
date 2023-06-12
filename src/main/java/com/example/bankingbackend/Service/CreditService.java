package com.example.bankingbackend.Service;

import com.example.bankingbackend.Entity.Credit;
import com.example.bankingbackend.repository.CreditRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

	@Autowired
    private CreditRepository creditRepository;
    
    public boolean checkCreditExistsWithAccNo(Long accountNo) {
        if(creditRepository.findByAccountNo(accountNo)!=null)
           return true;
        else 
           return false;
    }

    public Credit getCreditDetailsByAccNo(Long accountNo){
    	return creditRepository.findByAccountNo(accountNo);
    }

}
