package com.example.bankingbackend.Controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.Entity.LoginForm;
import com.example.bankingbackend.Entity.PasswordRequest;
import com.example.bankingbackend.Entity.UserInfo;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.DebitRepository;
import com.example.bankingbackend.repository.UserInfoRepository;
@CrossOrigin("*")

@RestController
@RequestMapping("/")
public class RegistrationController {
	@Autowired
	private UserInfoRepository UserInfoRepository;

	@Autowired
	private DebitRepository DebitRepository;
    private final EmailSenderService emailService;
    
    String customeridref;
    public RegistrationController(EmailSenderService emailService) {
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
//    	System.out.println(debit.getCvv());
//    	System.out.println(debit.getFirstName());
//    	System.out.println(debit.getValidFrom());
    	DebitRepository.save(debit);
    }
    
    @PostMapping("/api/password")
    public boolean updatePassword( @RequestBody PasswordRequest password) {
      UserInfo user = UserInfoRepository.findByCustomerId(customeridref).orElse(null);
//      System.out.println(customeridref);
//      System.out.println(" "+password.getPassword());
      if (user == null) {
        return false;
      }
      user.setpassword(password.getPassword());
      UserInfoRepository.save(user);
      return true;
    }
    @PostMapping("/api/checkCustomerId")
    public boolean customeridinfo(@RequestBody String customerId) {
    	Optional<Accounts> custid = AccountsRepository.findByCustomerId(customerId);
    	System.out.println(customerId);
		if (custid == null)
			return false;
		return true;
      }
    @PostMapping("/api/register")
    public void registerUser(@RequestBody UserInfo userInfo) {
//    public void registerUser(@RequestBody UserInfo userInfo) {
    	customeridref=userInfo.getCustomerId();
    	System.out.println(customeridref);
    	System.out.println(userInfo.getEmailId());
    	System.out.println(userInfo.getFirstName());
    	
    	UserInfoRepository.save(userInfo);
        // Save the user information in the database
        // Generate a verification link/token
        // Send the verification email
    	String verificationLink = "http://localhost:3000/passwordset";
//        String verificationLink = "http://www.google.com"; // Replace with your verification link
        emailService.sendVerificationEmail(userInfo.getEmailId(), verificationLink);
        System.out.println("Mail Send..");
    }
    @PostMapping("/api/login")
    	// Retrieve user by customerId or email
    	
    public boolean loginUser(@RequestBody LoginForm loginForm) {
//    public void registerUser(@RequestBody UserInfo userInfo) {
 
    	System.out.println(loginForm.getCustomerId());
    	System.out.println(loginForm.getEmailId());
    	System.out.println(loginForm.getPassword());
    	System.out.println(loginForm.getEmailId());
    	UserInfo user = UserInfoRepository.findByCustomerIdOrEmailId(loginForm.getCustomerId(), loginForm.getEmailId());

    	if (user != null) {
    	// Verify password
    	if (loginForm.getPassword().equals(user.getpassword())) {
    	// Authentication successful, return user details
//    		System.out.println(user.getpassword());
    		return true;
    	}
    	}
    	return false;	
//    	Optional<UserInfo> custid = UserInfoRepository.findByCustomerId(loginForm.getCustomerId());
////    	Optional<UserInfo> emailid = UserInfoRepository.findByCustomerId(userInfo.getEmailId());
////    	Optional<UserInfo> password = UserInfoRepository.findByCustomerId(userInfo.getpassword());
//		if (custid == null)
//			return false;
//		if (emailid == null)
//			return false;
//		if (password == null)
//			return false;
//		return true;
//    	UserInfoRepository.save(userInfo);
//        // Save the user information in the database
//        // Generate a verification link/token
//        // Send the verification email
//    	String verificationLink = "http://localhost:3000/passwordset";
////        String verificationLink = "http://www.google.com"; // Replace with your verification link
//        emailService.sendVerificationEmail(userInfo.getEmailId(), verificationLink);
//        System.out.println("Mail Send..");
    }
}