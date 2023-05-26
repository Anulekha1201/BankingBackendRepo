package com.example.bankingbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.repository.LoanRepository;

@Service
public class LoanService {
	@Autowired
	private LoanRepository loanRepository;
	
	public void applyLoan(Loans loan)
	{
		loanRepository.save(loan);	
	}
	
	public boolean checkUniqueCardNo(Long cardNo)
	{
		
		if(loanRepository.findByCardNo(cardNo) != null) {
			return true;
		}
		else {
			return false;
		}
		
	}
	

}
