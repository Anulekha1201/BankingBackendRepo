package com.example.bankingbackend.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.repository.AccountsRepository;




@Service
public class AccountService {

	@Autowired
	private AccountsRepository accountsRepository;
	
	public boolean checkAccountExists(Long accountNo)
	{
		Accounts accno = accountsRepository.findByAccountNo(accountNo);
    	if(accno==null) {
    		System.out.println("Account doesn't exist");
    		return false;
    	}
    	else {
    		if(accno.getStatus().equals("Active")) {
				return true;
			}
    		System.out.println("Account exists but in not inactive");
    		return false;
    	}
	}
	
	public Accounts getAccWithAccNo(Long accountNo)
	{
		Accounts accno = accountsRepository.findByAccountNo(accountNo);
		return accno;
		
	}
	
	public void saveAccounts(Accounts account)
	{
		accountsRepository.save(account);		
	}

	public List<Accounts> getAllAccounts() {
		// TODO Auto-generated method stub
		return accountsRepository.findAll();
	}

	public Accounts addAccounts(Accounts account) {
		// TODO Auto-generated method stub
		return accountsRepository.save(account);
	}
	
	public Accounts updateAccount(long id, Accounts account) {
		Accounts acc = accountsRepository.findById(id).get();
//		acc.setCustomerId(account.getCustomerId());
//		acc.setAccountNo(account.getAccountNo());
//		acc.setAccountType(account.getAccountType());
//		acc.setFirstName(account.getFirstName());
//		acc.setLastName(account.getLastName());
		acc.setEmailId(account.getEmailId());
		acc.setMobileNumber(account.getMobileNumber());
		acc.setAddress(account.getAddress());
		acc.setState(account.getState());
		acc.setCountry(account.getCountry());
		acc.setPanNumber(account.getPanNumber());
		acc.setBalance(account.getBalance());
		acc.setStatus(account.getStatus());
		
		Accounts updatedAccount = accountsRepository.save(acc);
		return updatedAccount;
	}
	
	public List<Accounts> getAccountByCustomerId(String customerId)  {
		
		List<Accounts> account = accountsRepository.findByCustomerId(customerId);
//		if (account.isEmpty())
//			throw new RecordNotFoundException("Account not found with this id: " + id);
		return account;
			 
	}
	
	public void deleteAccount(long id)  {
		
//		Optional<Accounts> account = accountsRepository.findById(id);
//		if(account.isEmpty())
//			throw new RecordNotFoundException("id not found");
		 accountsRepository.deleteById(id);
		
		
	}
	
}
