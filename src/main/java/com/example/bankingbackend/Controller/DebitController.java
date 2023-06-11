package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.BlockorUnBlockCard;
import com.example.bankingbackend.Entity.Debit;

import com.example.bankingbackend.Entity.Notifications;
import com.example.bankingbackend.Entity.setresetPin;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.NotificationsService;

import com.example.bankingbackend.Entity.TransactionHistory;
import com.example.bankingbackend.Entity.setresetPin;
import com.example.bankingbackend.Exception.BadRequestException;
import com.example.bankingbackend.Exception.ValidationException;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.TransactionHistoryService;

import com.example.bankingbackend.repository.DebitRepository;

@CrossOrigin("*")
@RestController
public class DebitController {

	@Autowired
	private DebitRepository debitRepository;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private NotificationsService notificationsService;
	
	private final EmailSenderService emailService;
	
	@Autowired
	public TransactionHistoryService transactionHistoryService; 

	public DebitController(EmailSenderService emailService) {
		this.emailService = emailService;
	}

	@GetMapping("/api/admindashboard/DebitapprovalsHistory")
	public List<Debit> DebitapprovalsHistory() {

		List<Debit> dh = debitRepository.findByStatus("Waiting for approval");
		System.out.println(dh);
		return dh;
	}

	@GetMapping("/api/admindashboard/DebitapprovedHistory")
	public List<Debit> DebitapprovedHistory() {

		List<Debit> dh1 = debitRepository.findByStatus("Approved");
		List<Debit> dh2 = debitRepository.findByStatus("Active");
		dh1.addAll(dh2);
		System.out.println(dh1);
		return dh1;
	}

	@PostMapping("/api/admindashboard/updatestatus/{cardNo}")
	public boolean updatestatustoapprove(@PathVariable Long cardNo) {
		Debit da = debitRepository.findByCardNo(cardNo);
		System.out.println(cardNo + " " + da.getStatus());

		
		Notifications n=notificationsService.getnotificationsDetails(da.getCardNo(),"Debit Card");
		
		n.setStatus("Approved");
		notificationsService.saveAccounts(n);
		
			da.setStatus("Approved");
			debitRepository.save(da);
			return true;

	}

	@PostMapping("/api/user/accountnocheck/{accountNo}")
	public boolean accNoCheck(@PathVariable Long accountNo) {

		return accountService.checkAccountExists(accountNo);
	}

	// applying for a new card
	@PostMapping("/api/user/applydebitcard") 
	public boolean saveDebit(@RequestBody Debit debit) throws ValidationException{
		Long accountno = debit.getAccountNo();
		System.out.println(debit.getStatus());
		Debit d = debitRepository.findByAccountNo(accountno);
		System.out.println(accountno + " " + d);
		if (d == null) {
			
			Notifications n=new Notifications();
			n.setEmailId(debit.getEmailId());
			n.setCardNo(debit.getCardNo());
			n.setNotificationType("Debit Card");
			n.setStatus("Waiting for approval");
			notificationsService.saveAccounts(n);
			debitRepository.save(debit);
			return true;
		} else {
			System.out.println("Customer already exists");
			//return false;
			throw new ValidationException("Customer already exists");
		}
	}

	// set a pin
	@PostMapping("/api/user/setorresetpin")
	public boolean setPin(@RequestBody setresetPin setpin) throws ValidationException{
		Debit debit = debitRepository.findByCardNo(setpin.getCardNo());
		System.out.println(setpin.getPinNo() + "-" + debit.getStatus() + "-");
//      if (debit== null) {
//		return false;
//      } 
//      else {
//		
//      }
		if (debit == null || debit.getStatus().equals("Waiting for approval")){
			System.out.println(setpin.getPinNo() + " " + debit.getStatus());
			//return false;
			throw new ValidationException("Waiting for approval");
		} else {
			Notifications n=notificationsService.getnotificationsDetails(debit.getCardNo(),"Debit Card");
			
			
			n.setStatus("Active");
			notificationsService.saveAccounts(n);
			debit.setPinNo(setpin.getPinNo());
			debit.setStatus(setpin.getStatus());
			System.out.println(debit.getPinNo() + " " + debit.getStatus());
			debitRepository.save(debit);
			emailService.sendVerificationEmailforsetpin(debit.getEmailId(), debit.getCardNo(), debit.getPinNo());
			System.out.println("Mail Send..");
			return true;
		}
	}

	// blocking a debitCard
	@PostMapping("/api/user/blockcard")
	public boolean blockDebitCard(@RequestBody BlockorUnBlockCard blockcard) {
		Debit debit = debitRepository.findByCardNo(blockcard.getCardNo());
		System.out.println(blockcard.getCardNo());
		if (debit == null) {
			throw new BadRequestException("Debit card with given card number doesn't exists");
		} 
		else if(debit.getCvv()!=blockcard.getCvv() || debit.getPinNo() != blockcard.getPinNo())
			throw new BadRequestException("Invalid Pin or CVV");
		else if(debit.getStatus()!="Active") {
			throw new BadRequestException("Debit card with given card number is already blocked");
		}
		else {
			Notifications n=notificationsService.getnotificationsDetails(debit.getCardNo(),"Debit Card");
			
			
			n.setStatus("Block");
			notificationsService.saveAccounts(n);
			debit.setStatus(blockcard.getStatus());
			debitRepository.save(debit);
			
			emailService.sendVerificationEmailforBlockPin(debit.getEmailId(), debit.getCardNo());
			System.out.println("Mail Send..");

			return true;
		}
	}

	@PostMapping("/api/user/Unblockcard")

	public boolean UnblockDebitCard(@RequestBody BlockorUnBlockCard Unblockcard) throws BadRequestException{
		Debit debit = debitRepository.findByCardNo(Unblockcard.getCardNo());
		System.out.println(Unblockcard.getCardNo());
		if (debit == null) {
			//return false;
			throw new BadRequestException("Debit card number must not be null please, Enter the correct Debit card number");
		} else {
			Notifications n=notificationsService.getnotificationsDetails(debit.getCardNo(),"Debit Card");
			
			n.setStatus("Active");
			notificationsService.saveAccounts(n);
			debit.setStatus(Unblockcard.getStatus());
			debitRepository.save(debit);

			emailService.sendVerificationEmailforUnBlockPin(debit.getEmailId(), debit.getCardNo());
			System.out.println("Mail Send..");

			return true;

		}
	}

//      @GetMapping("api/user/transactionHistory/{accountNo}")
//  	public List<TransactionHistory> gettransactionHistory(@PathVariable Long accountNo)
//  	{
//		List<TransactionHistory> th= transactionHistoryService.getTransactionHistoryForAcc(accountNo);
//  		
//  		return th;
//  	}

}