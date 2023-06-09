package com.example.bankingbackend.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.Loans;
import com.example.bankingbackend.Entity.TransactionHistory;
import com.example.bankingbackend.Exception.BadRequestException;
import com.example.bankingbackend.Exception.ResourceNotFoundException;
import com.example.bankingbackend.Exception.ValidationException;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.LoanService;
import com.example.bankingbackend.Service.TransactionHistoryService;

@CrossOrigin("*")
@RestController
public class TransactionController {
	@Autowired
	public LoanService loanService;
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionHistoryService transactionHistoryService;
	
	@PutMapping("/api/user/transactions/deposit/{accountNo}/{amount}")
    public boolean deposit(@PathVariable Long accountNo, @PathVariable Long amount) throws ResourceNotFoundException 
	{
		boolean accExists= accountService.checkAccountExists(accountNo);
		System.out.println("in meth");
		if(!accExists)
		{
			//System.out.println("Account doesn't exists.");
			//return false;
			throw new ResourceNotFoundException("Account doesnot exists");
		}
		else
		{
			
			Accounts account= accountService.getAccWithAccNo(accountNo);
			account.setBalance(account.getBalance()+amount);
			accountService.saveAccounts(account);
			System.out.println("Deposited : "+amount);
			long timestamp = System.currentTimeMillis();
	        Date date = new Date(timestamp);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String sqlDate = dateFormat.format(date);
//			LocalDateTime currentDateTime = LocalDateTime.now();
//	        Timestamp sqlDate = Timestamp.valueOf(currentDateTime);
//	        System.out.println(sqlDate);
			TransactionHistory t = new TransactionHistory(null, accountNo, "Deposit", amount, accountNo, "success", sqlDate);
			transactionHistoryService.addTransactionHistory(t);
			return true;
		}
	}

	@PutMapping("/api/user/transactions/withDrawal/{accountNo}/{amount}")
    public boolean withDrawal(@PathVariable Long accountNo, @PathVariable Long amount) throws  ResourceNotFoundException, ValidationException
	{
		boolean accExists= accountService.checkAccountExists(accountNo);
		
		if(!accExists)
		{
			//System.out.println("Account doesn't exists.");
			//return false;
			throw new ResourceNotFoundException("Account doesnot exists");
		}
		else
		{
			Accounts account= accountService.getAccWithAccNo(accountNo);
			float balance = account.getBalance();
			if(balance<amount)
			{
				//System.out.println("Balance is lower than withdrawal amount");
				//return false;
				throw new ValidationException("Balance is lower than withdrawal amount");
			}
			else
			{
				account.setBalance(account.getBalance()-amount);
				accountService.saveAccounts(account);
				System.out.println("Withdrawn : "+amount);
//				LocalDate currentDate = LocalDate.now();
//		        Date sqlDate = Date.valueOf(currentDate);
//				LocalDateTime currentDateTime = LocalDateTime.now();
//		        Timestamp sqlDate = Timestamp.valueOf(currentDateTime);
				long timestamp = System.currentTimeMillis();
		        Date date = new Date(timestamp);
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String sqlDate = dateFormat.format(date);
		        System.out.println(sqlDate);
				TransactionHistory t = new TransactionHistory(null, accountNo, "WithDrawal", amount, accountNo, "success",sqlDate);
				transactionHistoryService.addTransactionHistory(t);
				
				return true;
			}
		}
    	
	}
	
	@PutMapping("/api/user/transactions/transfer/{accountNoFrom}/{accountNoTo}/{amount}")
    public boolean transfer(@PathVariable Long accountNoFrom, @PathVariable Long accountNoTo, @PathVariable Long amount) throws ResourceNotFoundException, ValidationException,BadRequestException 
	{
		boolean accExists= accountService.checkAccountExists(accountNoFrom);
		boolean accExists2= accountService.checkAccountExists(accountNoTo);
		
		if(!(accExists && accExists2))
		{
			System.out.println("Account doesn't exists."+(accExists && accExists2));
			return false;
			//throw new ResourceNotFoundException("Account doesn't exists."+(accExists && accExists2));
		}
		else
		{
			Accounts accountFrom= accountService.getAccWithAccNo(accountNoFrom);
			Accounts accountTo= accountService.getAccWithAccNo(accountNoTo);
			float balanceFrom = accountFrom.getBalance();
			if(balanceFrom<amount)
			{
				//System.out.println("Insufficient balance");
				//return false;
				throw new BadRequestException("Insufficient Balance");
			}
			else
			{
				accountFrom.setBalance(accountFrom.getBalance()-amount);
				accountTo.setBalance(accountTo.getBalance()+amount);
				accountService.saveAccounts(accountTo);
				accountService.saveAccounts(accountFrom);
				System.out.println("Transfered : "+amount);
//				LocalDateTime currentDateTime = LocalDateTime.now();
//		        Timestamp sqlDate = Timestamp.valueOf(currentDateTime);
				long timestamp = System.currentTimeMillis();
		        Date date = new Date(timestamp);
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String sqlDate = dateFormat.format(date);
		        System.out.println(sqlDate);
				TransactionHistory t = new TransactionHistory(null, accountNoFrom, "Transfer", amount, accountNoTo, "success", sqlDate);
				transactionHistoryService.addTransactionHistory(t);
				
				return true;
			}
		}
    	
	}

	@GetMapping("api/user/transactionHistory/{accountNo}")
	public List<TransactionHistory> gettransactionHistory(@PathVariable Long accountNo) throws ResourceNotFoundException, ValidationException
	{
		List<TransactionHistory> th= transactionHistoryService.getTransactionHistoryForAcc(accountNo);
		System.out.println("transaction history: "+th);
		return th;
	}
	
	@GetMapping("api/user/checkIfLoanExistsByLoanId/{loanId}")
	public boolean checkIfLoanExistsByLoanId(@PathVariable Long loanId)
	{
		Loans l=loanService.getLoanByLoanId(loanId);
		if(l==null)
		{
			//System.out.println("Loan dosn't exists with this loan id");
			//return false;
			throw new ResourceNotFoundException("Loan doesn't exists with loan id: "+loanId);
		}
		return true;	
	}
	
	@SuppressWarnings("null")
	@GetMapping("api/user/getInstallmentByLoanId/{loanId}")
	public float getLoanInstallmentByLoanId(@PathVariable Long loanId)
	{
		float l=loanService.getInstallmentByLoanId(loanId);
		return l;	
	}
	
	@PutMapping("api/user/payLoanTransaction/{loanId}/{accountNo}")
	public boolean payLoan(@PathVariable Long loanId, @PathVariable Long accountNo) {
		    float l = loanService.getInstallmentByLoanId(loanId);
		    
		    long timestamp = System.currentTimeMillis();
	        Date date = new Date(timestamp);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String sqlDate = dateFormat.format(date);
	        
	        System.out.println(sqlDate);
			
	        
		    Loans loan= loanService.getLoanByLoanId(loanId);
			Accounts account= accountService.getAccWithAccNo(accountNo);
			if(account.getBalance()>=l) {
				account.setBalance(account.getBalance()-l);
				accountService.saveAccounts(account);
				
				TransactionHistory t = new TransactionHistory(null, accountNo, "Loan", l, accountNo, "success", sqlDate);
				transactionHistoryService.addTransactionHistory(t);
				loan.setBalanceAmt(loan.getTotalLoanAmt()-l);
				loanService.applyLoan(loan);
				System.out.println("Paid loan : "+l);
				return true;
			}
			else
			{
				TransactionHistory t = new TransactionHistory(null, accountNo, "Loan", l, accountNo, "Failed", sqlDate);
				transactionHistoryService.addTransactionHistory(t);
				System.out.println("Payment Unsuccessful. Insufficient balance");
				return false;
			}
		
	}
	

}