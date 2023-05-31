package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.LoanService;
@CrossOrigin("*")
@RestController

public class AccountController {
	
	@Autowired
	public AccountService accountService;

	@GetMapping("api/admin/accounts")
	public List<Accounts> getAllAccounts() {
		return accountService.getAllAccounts();
	}
	//Adding Employee
	@PostMapping("api/admin/addAccount")
	public Accounts addAccount(@RequestBody Accounts account)  {
		return accountService.addAccounts(account);
		
	}
	
	
}
