package com.example.bankingbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Service.AccountService;

@CrossOrigin("*")

@RestController
public class TransactionController {
	
	@Autowired
	private AccountService accountService;
	
	@PutMapping("/api/transactions/deposit/{accountNo}/{amount}")
    public boolean deposit(@PathVariable String accountNo, @PathVariable Long amount) 
	{
		boolean accExists= accountService.checkAccountExists(accountNo);
		
		if(!accExists)
		{
			System.out.println("Account doesn't exists.");
			return false;
		}
		else
		{
			Accounts account= accountService.getAccWithAccNo(accountNo);
			account.setBalance(account.getBalance()+amount);
			accountService.saveAccounts(account);
			System.out.println("Deposited : "+amount);
			
			return true;
		}
	}

	@PutMapping("/api/transactions/withDrawal/{accountNo}/{amount}")
    public boolean withDrawal(@PathVariable String accountNo, @PathVariable Long amount) 
	{
		boolean accExists= accountService.checkAccountExists(accountNo);
		
		if(!accExists)
		{
			System.out.println("Account doesn't exists.");
			return false;
		}
		else
		{
			Accounts account= accountService.getAccWithAccNo(accountNo);
			Long balance = account.getBalance();
			if(balance<amount)
			{
				System.out.println("Balance is lower than withdrawal amount");
				return false;
			}
			else
			{
				account.setBalance(account.getBalance()-amount);
				accountService.saveAccounts(account);
				System.out.println("Withdrawn : "+amount);
				return true;
			}
		}
    	
	}
	
	@PutMapping("/api/transactions/transfer/{accountNoFrom}/{accountNoTo}/{amount}")
    public boolean transfer(@PathVariable String accountNoFrom, @PathVariable String accountNoTo, @PathVariable Long amount) 
	{
		boolean accExists= accountService.checkAccountExists(accountNoFrom);
		boolean accExists2= accountService.checkAccountExists(accountNoTo);
		
		if(!(accExists && accExists2))
		{
			System.out.println("Account doesn't exists.");
			return false;
		}
		else
		{
			Accounts accountFrom= accountService.getAccWithAccNo(accountNoFrom);
			Accounts accountTo= accountService.getAccWithAccNo(accountNoTo);
			Long balanceFrom = accountFrom.getBalance();
			if(balanceFrom<amount)
			{
				System.out.println("Insufficient balance");
				return false;
			}
			else
			{
				accountFrom.setBalance(accountFrom.getBalance()-amount);
				accountTo.setBalance(accountTo.getBalance()+amount);
				accountService.saveAccounts(accountTo);
				accountService.saveAccounts(accountFrom);
				System.out.println("Transfered : "+amount);
				return true;
			}
		}
    	
	}
}