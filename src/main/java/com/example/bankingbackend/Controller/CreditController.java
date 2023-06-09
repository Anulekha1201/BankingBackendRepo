package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.BlockorUnBlockCard;
import com.example.bankingbackend.Entity.Credit;
import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.setresetPin;
import com.example.bankingbackend.Exception.BadRequestException;
import com.example.bankingbackend.Service.CreditService;
import com.example.bankingbackend.repository.CreditRepository;

import jakarta.validation.ValidationException;

@RestController
public class CreditController {
	
	
	private final EmailSenderService emailService ;
	
	@Autowired
	public CreditService cs;
	
	@Autowired
	public CreditRepository creditrepository;
	

	public CreditController(EmailSenderService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping("/getcredit")
	public List<Credit> getCredit(){
		return cs.getcredit();
	}
	
	@GetMapping("/api/admindashboard/CreditapprovalsHistory")
	public List<Credit> CreditapprovalsHistory() {

		List<Credit> ch = creditrepository.findByStatus("Waiting for approval");
		System.out.println(ch);
		return ch;
	}
	
	
	@GetMapping("/api/admindashboard/CreditApprovedHisory")
	public List<Credit> CreditApprovedHistory(){
		
		List<Credit> ch1 = creditrepository.findByStatus("Approved");
		List<Credit> ch2 = creditrepository.findByStatus("Active");
		ch1.addAll(ch2);
		System.out.println(ch1);
		return ch1;
		
	}
	
	@PostMapping("/api/user/applycreditcard")
	public boolean saveCredit(@RequestBody Credit credit) {
		Long accountNo = credit.getAccountNo();
		System.out.println(credit.getStatus());
		Credit cr = creditrepository.findByAccountNo(accountNo);
		System.out.println(accountNo+" "+cr);
		if(cr == null) {
			creditrepository.save(credit);
			return true;
		}
		else {
			throw new BadRequestException("Credit card with this account number already exists");
		}
	}
	
	
	@PostMapping("/api/user/blockcreditcard")
	public boolean BlockCreditCard(@RequestBody BlockorUnBlockCard blockCard) {
		Credit credit = creditrepository.findByCardNo(blockCard.getCardNo());
		System.out.println(blockCard.getCardNo());
		if(credit == null) {
			throw new BadRequestException("Please enter the correct credit card number");
		}
		else {
			credit.setStatus(blockCard.getStatus());
			creditrepository.save(credit);
			
			emailService.sendVerificationEmailforBlockPin(credit.getEmailId(), credit.getCardNo());
			System.out.println("Mail Sent..");
			
			return true;
		}
		
	}
	
	@PostMapping("/api/user/unblockcreditcard")
	public boolean UnblockCreditCard(@RequestBody BlockorUnBlockCard unblockcard) {
		Credit credit = creditrepository.findByCardNo(unblockcard.getCardNo());
		System.out.println(unblockcard.getCardNo());
		if(credit == null) {
			throw new BadRequestException("Please enter the correct credit card number");
		}
		else {
			credit.setStatus(unblockcard.getStatus());
			creditrepository.save(credit);
			
			emailService.sendVerificationEmailforUnBlockPin(credit.getEmailId(), credit.getCardNo());
			System.out.println("Mail Sent..");
			
			return true;
		}
	}
	
	@PostMapping("/api/user/setorresetpinforcredit")
	public boolean setPin(@RequestBody setresetPin setPin) {
		Credit credit = creditrepository.findByCardNo(setPin.getCardNo());
		if( credit == null || credit.getStatus().equals("Waiting for approval..")) {
			System.out.println(setPin.getPinNo() + " " + credit.getStatus());
			throw new ValidationException("The credit card is not approved yet");
		}
		else {
			credit.setPinNo(setPin.getPinNo());
			credit.setStatus(setPin.getStatus());
			System.out.println(credit.getPinNo()+" "+ credit.getStatus());
			creditrepository.save(credit);
			emailService.sendVerificationEmailforsetpin(credit.getEmailId(), credit.getCardNo(), credit.getPinNo());
			System.out.println("Mail Sent..");
			
			return true;
		}
	}

}
