package com.example.bankingbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.Exception.ResourceNotFoundException;
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

	public boolean checkIfLoanExistsWithDebitCardNo(Long cardNo) throws ResourceNotFoundException {
		if (loanRepository.findByCardNo(cardNo) == null) {
			return true;
		} else {
			throw new ResourceNotFoundException("The loan doesn't exsists with this card number");
		}

	}

	public Loans getLoanByLoanId(Long loanId)throws ResourceNotFoundException {
		Loans l = loanRepository.findByLoanId(loanId);
		if (l != null) {
			return l;
		} else {
			throw new ResourceNotFoundException("Loan for loan id: "+loanId+" doesn't exists");
			//return null;
		}

	}

	@SuppressWarnings("null")
	public float getInstallmentByLoanId(Long loanId) {
		Loans l = loanRepository.findByLoanId(loanId);
		if (l != null) {

			return l.getInstallment();
		}
		throw new ResourceNotFoundException("The installments for this loan id doesn't exists");
		//return (Float) null;

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
