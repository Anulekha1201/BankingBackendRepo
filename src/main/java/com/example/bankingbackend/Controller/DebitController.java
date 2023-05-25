package com.example.bankingbackend.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.BlockorUnBlockCard;
import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.setresetPin;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.DebitRepository;
@CrossOrigin("*")

@RestController
public class DebitController {

	@Autowired
	private DebitRepository debitRepository;
	
	@Autowired
	private AccountService accountService;
	
	private final EmailSenderService emailService;
	
	public DebitController(EmailSenderService emailService) {
        this.emailService = emailService;
    }

	@PostMapping("/api/accountnocheck/{accountNo}")
    public boolean accNoCheck(@PathVariable String accountNo) {
    	
		return accountService.checkAccountExists(accountNo);
	}
	
  //applying for a new card
    @PostMapping("/api/applydebitcard")
    public boolean saveDebit(@RequestBody Debit debit) {
    	Long accountno=debit.getAccountNo();
    	Debit d=debitRepository.findByAccountNo(accountno);
    	System.out.println(accountno+" "+d);
    	if(d==null) {
    		debitRepository.save(debit);
    		return true;
    	}
    	else {
    		System.out.println("Customer already exists");
    		return false;
    	}
    }
  //set a pin
    @PostMapping("/api/setorresetpin")
    public boolean setPin(@RequestBody setresetPin setpin) {
    	Debit debit=debitRepository.findByCardNo(setpin.getCardNo());    	
      System.out.println(setpin.getPinNo());
      if (debit== null) {
        return false;
      }
      debit.setPinNo(setpin.getPinNo());
      debit.setStatus(setpin.getStatus());
      System.out.println(debit.getPinNo()+" "+debit.getStatus());
      debitRepository.save(debit);
      emailService.sendVerificationEmailforsetpin(debit.getEmailId(), debit.getCardNo(),debit.getPinNo());
      System.out.println("Mail Send..");
      
      return true;
      }
  //blocking a debitCard
    @PostMapping("/api/blockcard")
    public boolean blockDebitCard(@RequestBody BlockorUnBlockCard blockcard) {
    	Debit debit=debitRepository.findByCardNo(blockcard.getCardNo());
    	System.out.println(blockcard.getCardNo());
      if (debit == null) {
        return false;
      }
      else{
      debit.setStatus(blockcard.getStatus());
      debitRepository.save(debit);
      
      emailService.sendVerificationEmailforBlockPin(debit.getEmailId(), debit.getCardNo());
      System.out.println("Mail Send..");
      
      return true;
      }
    }
    @PostMapping("/api/Unblockcard")
    
    public boolean UnblockDebitCard(@RequestBody BlockorUnBlockCard Unblockcard) {
    	Debit debit=debitRepository.findByCardNo(Unblockcard.getCardNo());
    	System.out.println(Unblockcard.getCardNo());
      if (debit == null) {
        return false;
      }
      else{
      debit.setStatus(Unblockcard.getStatus());
      debitRepository.save(debit);
      
      emailService.sendVerificationEmailforUnBlockPin(debit.getEmailId(), debit.getCardNo());
      System.out.println("Mail Send..");
      
      return true;
    	
    }
    }
}