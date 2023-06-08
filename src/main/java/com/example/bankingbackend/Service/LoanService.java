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
		loan.setStatus("Waiting for approval");
		loanRepository.save(loan);	
	}
	
	public boolean checkIfLoanExistsWithDebitCardNo(Long cardNo)
	{
		if(loanRepository.findByCardNo(cardNo) == null) {
			return true;
		}
		else {
			return false;
		}
		
	}
    
	public Loans getLoanByLoanId(Long loanId)
	{
		Loans l = loanRepository.findByLoanId(loanId);
		if(l!=null)
		{
			return l;
		}
		return null;
	}
	
	
	@SuppressWarnings("null")
	public float getInstallmentByLoanId(Long loanId)
	{
		Loans l = loanRepository.findByLoanId(loanId);
		if(l!=null)
		{
		
		return l.getInstallment();
		}
		
		return (Float) null;
		
	}
	
	public Loans getLoanDetailsByCardNo(Long cardNo)
	{
		Loans l = loanRepository.findByCardNo(cardNo);
		if(l!=null)
		{
			return l;
		}
		return null;
	}
}
