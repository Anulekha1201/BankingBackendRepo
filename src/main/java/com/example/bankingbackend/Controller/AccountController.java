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
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.LoanService;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class AccountController {
	
	@Autowired
	public AccountService accountService;

	@GetMapping("api/admin/accounts")
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
	
	@GetMapping("api/admin/getAccountById/{id}")
    public Optional<Accounts> getAccountById(@PathVariable long id)  {
		
		Optional<Accounts> account = accountService.getAccountById(id);
		return account;
    }	
	
	@DeleteMapping("api/admin/deleteAccount/{id}")
	public void deleteAccountById(@PathVariable long id) {
		accountService.deleteAccount(id);
		
	}
    
    
}
