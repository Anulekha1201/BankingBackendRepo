package com.example.bankingbackend.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.DebitService;
import com.example.bankingbackend.Service.LoanService;

@CrossOrigin("*")
@RestController

public class AccountController {
	
	@Autowired
	public AccountService accountService;
	
	@Autowired
	public DebitService debitService;
	
	@Autowired
	public LoanService loanService;
	
	@GetMapping("/api/admin/accounts")
	public List<Accounts> getAllAccounts() {
		return accountService.getAllAccounts();
	}
	
	@PostMapping("api/admin/addAccount")
	public Accounts addAccount(@RequestBody Accounts account)  {
		return accountService.addAccounts(account);	
	}
	
	@PutMapping("api/admin/updateAccount/{id}")
	public Accounts updateAccount(@PathVariable Long id,@RequestBody Accounts account)
	{
		return accountService.updateAccount(id,account);
	}
	
	@GetMapping("/api/admin/getAccountById/{customerId}")
    public List<Accounts> getAccountById(@PathVariable String custid)  {
		
		List<Accounts> account = accountService.getAccountByCustomerId(custid);
		return account;
    }
	
	@GetMapping("/api/user/getAccountById/{customerId}")
    public List<Accounts> getUserAccountById(@PathVariable String customerId)  {
		
		List<Accounts> account = accountService.getAccountByCustomerId(customerId);
		return account;
    }
	
	@GetMapping("/api/user/login/getDebitDetails/{accountNo}")
    public Debit getDebitDetails(@PathVariable Long accountNo)  {
		
		Debit debit = debitService.getDebitDetailsByAccNo(accountNo);
		System.out.println(debit);
		return debit;
    }
	
	@GetMapping("/api/user/login/getLoanDetails/{cardNo}")
    public Loans getLoanDetails(@PathVariable Long cardNo)  {
		
		Loans loan = loanService.getLoanDetailsByCardNo(cardNo);
		System.out.println(loan);
		return loan;
    }
	
	
	@DeleteMapping("api/admin/deleteAccount/{id}")
	public void deleteAccountById(@PathVariable long id) {
		accountService.deleteAccount(id);
		
	}

	

}
