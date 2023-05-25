package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.TransactionHistory;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.TransactionHistoryService;
import com.example.bankingbackend.repository.TransactionHistoryRepository;

@CrossOrigin("*")
@RestController
public class TransactionController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionHistoryService transactionHistoryService;
	
	@PutMapping("/api/transactions/deposit/{accountNo}/{amount}")
    public boolean deposit(@PathVariable Long accountNo, @PathVariable Long amount) 
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
			TransactionHistory t = new TransactionHistory(null, accountNo, "Deposit", amount, accountNo, "success", null);
			transactionHistoryService.addTransactionHistory(t);
			return true;
		}
	}

	@PutMapping("/api/transactions/withDrawal/{accountNo}/{amount}")
    public boolean withDrawal(@PathVariable Long accountNo, @PathVariable Long amount) 
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
				TransactionHistory t = new TransactionHistory(null, accountNo, "WithDrawal", amount, accountNo, "success", null);
				transactionHistoryService.addTransactionHistory(t);
				
				return true;
			}
		}
    	
	}
	
	@PutMapping("/api/transactions/transfer/{accountNoFrom}/{accountNoTo}/{amount}")
    public boolean transfer(@PathVariable Long accountNoFrom, @PathVariable Long accountNoTo, @PathVariable Long amount) 
	{
		boolean accExists= accountService.checkAccountExists(accountNoFrom);
		boolean accExists2= accountService.checkAccountExists(accountNoTo);
		
		if(!(accExists && accExists2))
		{
			System.out.println("Account doesn't exists."+(accExists && accExists2));
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
				TransactionHistory t = new TransactionHistory(null, accountNoFrom, "Transfer", amount, accountNoTo, "success", null);
				transactionHistoryService.addTransactionHistory(t);
				
				return true;
			}
		}
    	
	}

	@GetMapping("api/transactionHistory/{accountNo}")
	public List<TransactionHistory> gettransactionHistory(@PathVariable Long accountNo)
	{
		List<TransactionHistory> th= transactionHistoryService.getTransactionHistoryForAcc(accountNo);
		
		return th;
	}
}