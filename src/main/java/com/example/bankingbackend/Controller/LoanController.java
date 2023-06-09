package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.Entity.Notifications;
import com.example.bankingbackend.Service.DebitService;
import com.example.bankingbackend.Service.LoanService;
import com.example.bankingbackend.Service.NotificationsService;
import com.example.bankingbackend.repository.LoanRepository;
@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class LoanController {
	
	@Autowired
	public LoanService loanService;
	
	@Autowired
	public DebitService debitService;
	
	@Autowired 
	public LoanRepository loanrepository;
	
	@Autowired
	private NotificationsService notificationsService;
//	@GetMapping("/getloans")
//	public List<Loans> getLoans(){
//		return loanRepository.findAll();
//	}
//	
//	@GetMapping("/getloan/{loanId}")
//	public Loans getLoanById(@PathVariable Long loanId) {
//		return loanRepository.findById(loanId).orElse(null);
//	}
//	
	@GetMapping("/api/admindashboard/LoanapprovalsHistory")
	public List<Loans> LoanapprovalsHistory() {

		List<Loans> dh = loanrepository.findByStatus("Waiting for approval");
		System.out.println(dh);
		return dh;
	}

	@GetMapping("/api/admindashboard/LoanapprovedHistory")
	public List<Loans> DebitapprovedHistory() {

		List<Loans> dh = loanrepository.findByStatus("Approved");
		
		System.out.println(dh);
		return dh;
	}

	@PostMapping("/api/admindashboard/updateLoanstatus/{cardNo}")
	public boolean updatestatustoapprove(@PathVariable Long cardNo) {
		Loans da = loanrepository.findByCardNo(cardNo);
		System.out.println(cardNo + " " + da.getStatus());
		
		Debit d=debitService.getDebitDetails(cardNo);
		System.out.println(d.getEmailId());
		
		Notifications n=notificationsService.getnotificationsDetails(cardNo,"Loan");
		
		n.setStatus("Approved");
		notificationsService.saveAccounts(n);
		
		da.setStatus("Approved");
		loanrepository.save(da);
		return true;

	}
	@PostMapping("api/user/applyLoan")
	public boolean addLoan(@RequestBody Loans loan ){
		Long cardNo= loan.getCardNo();
		System.out.println("carNo: "+loan.getCardNo());
		if(debitService.checkDebitExists(cardNo))
		{
			if(loanService.checkIfLoanExistsWithDebitCardNo(cardNo))
			{
				System.out.println(loanService.checkIfLoanExistsWithDebitCardNo(cardNo));
				System.out.println(debitService.checkDebitExists(cardNo)+"\ncardNo: "+cardNo);
				System.out.println("cardNo: "+loan.getCardNo());
				
				Debit d=debitService.getDebitDetails(cardNo);
				
				Notifications n=new Notifications();
				n.setEmailId(d.getEmailId());
				n.setCardNo(d.getCardNo());
				
				n.setNotificationType("Loan");
				n.setStatus("Waiting for approval");
				notificationsService.saveAccounts(n);
				
				loanService.applyLoan(loan);
				System.out.println("loan applied");
				return true;
			}
			else
			{
				System.out.println("loan already exists with this cardNo");
				return false;
			}
			
		}
		else
		{
			System.out.print("Debit Card number doesn't exist");
			return false;
		}
	}
	
	
	
	

//	@PutMapping("/update/{loanId}")
//	public String updateLoan(@PathVariable Long loanId, @RequestBody Loans loan) {
//		Loans existingLoan = loanRepository.findById(loanId).orElse(null);
//		if( existingLoan ==  null) {
//			return "Loan Id "+loanId+" does not exist";
//		}
//		//Update specific fields
//		if(loan.getLoanType() != null) {
//			existingLoan.setLoanType(loan.getLoanType());
//		}
//		
//		if(loan.getFirstName() != null) {
//			existingLoan.setFirstName(loan.getFirstName());
//		}
//		
//		if(loan.getLastName() != null) {
//			existingLoan.setLastName(loan.getLastName());
//		}
//		//update the following fields as required
//		if(loan.getTenure() != null) {
//			existingLoan.setTenure(loan.getTenure());
//		}
//		
//		if(loan.getInstallment() != null) {
//			existingLoan.setInstallment(loan.getInstallment());
//		}
//		
//		if(loan.getInterestRate() != 0.0) {
//			existingLoan.setInterestRate(loan.getInterestRate());
//		}
//		
//		if(loan.getTotalLoanAmt() != null) {
//			existingLoan.setTotalLoanAmt(loan.getTotalLoanAmt());
//		}
//		
//		loanRepository.save(existingLoan);
//		return "Loan details are updated for LoanId : "+loanId;
//	}
//	
//	@DeleteMapping("/delLoan/{loanId}")
//	public String delLoan(@PathVariable Long loanId) {
//		loanRepository.deleteById(loanId);
//		return "Deleted loan of id: "+loanId;
//	}
}
