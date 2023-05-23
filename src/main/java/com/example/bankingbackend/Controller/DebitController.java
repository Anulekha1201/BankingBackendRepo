package com.example.bankingbackend.Controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.BlockCard;
import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.UserInfo;
import com.example.bankingbackend.Entity.setresetPin;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.DebitRepository;
@CrossOrigin("*")

@RestController
@RequestMapping("/")
public class DebitController {

	@Autowired
	private DebitRepository DebitRepository;
	
	private final EmailSenderService emailService;
	
	public DebitController(EmailSenderService emailService) {
        this.emailService = emailService;
    }
	@PostMapping("/api/accountnocheck")
    public boolean accNoCheck(@RequestBody String accountNo) {
    	Optional<Accounts> custid = AccountsRepository.findByCustomerId(accountNo);
    	System.out.println(accountNo);
    	if (custid == null)
    		return false;
    	return true;
    	}
  //applying for a new card
    @PostMapping("/api/applydebitcard")
    public void saveDebit(@RequestBody Debit debit) {

    	DebitRepository.save(debit);
    }
  //set a pin
    @PostMapping("/api/setorresetpin")
    public boolean setPin(@RequestBody setresetPin setpin) {
    	Debit debit=DebitRepository.findByCardNo(setpin.getCardNo());    	
//      System.out.println(setpin.getCardNo());

      if (debit == null) {
        return false;
      }

      
      DebitRepository.save(debit);
      
      emailService.sendVerificationEmailforsetpin(debit.getEmailId(), debit.getCardNo(),debit.getPinNo());
      System.out.println("Mail Send..");
      
      return true;
      }
  //blocking a debitCard
    @PostMapping("/api/blockcard")
    public boolean blockDebitCard(@RequestBody BlockCard blockcard) {
    	Debit debit=DebitRepository.findByCardNo(blockcard.getCardNo());
      if (debit == null) {
        return false;
      }
      debit.setStatus(blockcard.getStatus());
      DebitRepository.save(debit);
      
      emailService.sendVerificationEmailforBlockPin(debit.getEmailId(), debit.getCardNo());
      System.out.println("Mail Send..");
      
      return true;
    }


}