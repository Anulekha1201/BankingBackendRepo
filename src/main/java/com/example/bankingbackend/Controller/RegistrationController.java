package com.example.bankingbackend.Controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.LoginForm;
import com.example.bankingbackend.Entity.PasswordRequest;
import com.example.bankingbackend.Entity.Support;
import com.example.bankingbackend.Entity.UserInfo;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.DebitRepository;
import com.example.bankingbackend.repository.UserInfoRepository;
@CrossOrigin("*")

@RestController
public class RegistrationController {
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private DebitRepository DebitRepository;
	
	@Autowired
	private AccountsRepository accountsRepository;
	
    private final EmailSenderService emailService;
    
    String customeridref;
    public RegistrationController(EmailSenderService emailService) {
        this.emailService = emailService;
    }
    
    @PostMapping("/api/password")
    public boolean updatePassword( @RequestBody PasswordRequest password) {
    	System.out.println(password.getCustomerId()+" "+password.getPassword());

      UserInfo user = userInfoRepository.findByCustomerId(password.getCustomerId());
//      System.out.println(user);
      if (user == null) {
        return false;
      }
      user.setpassword(password.getPassword());
      userInfoRepository.save(user);
      return true;
    }
    @PostMapping("/api/checkCustomerId/{customerId}")
    public boolean customeridinfo(@PathVariable String customerId) {
    	
    	
    	List<Accounts> custid = accountsRepository.findByCustomerId(customerId);
    	
    	if(custid.isEmpty()) {
    		return false;
    	}
    	else {
    		for(int i=0;i<custid.size();i++) {
    			
    			if(custid.get(i).getStatus().equals("Active")) {
//    				System.out.println(custid.get(i).getStatus()+" "+custid.get(i).getAccountNo());
					return true;
				}
    		}
    		return false;
    	}
      }
    @PostMapping("/api/register")
    public void registerUser(@RequestBody UserInfo userInfo) {
//    public void registerUser(@RequestBody UserInfo userInfo) {
    	customeridref=userInfo.getCustomerId();
    	
    	UserInfo user=userInfoRepository.findByCustomerId(customeridref);
    	if(user==null) {
    	userInfoRepository.save(userInfo);
    	String verificationLink = "http://localhost:3000/passwordset";
        emailService.sendVerificationEmail(userInfo.getEmailId(), verificationLink);
        System.out.println("Mail Send..");
    	}
    	else {
    		System.out.println("Customer already exists");
    	}
    }
    
    @PostMapping("/api/login")    	
    public boolean loginUser(@RequestBody LoginForm loginForm) {
 
    	System.out.println(loginForm.getCustomerId());
    	System.out.println(loginForm.getEmailId());
    	System.out.println(loginForm.getPassword());
    	System.out.println(loginForm.getEmailId());
    	UserInfo user = userInfoRepository.findByCustomerIdOrEmailId(loginForm.getCustomerId(), loginForm.getEmailId());
    	if (user != null) {
    	// Verify password
    	if (loginForm.getPassword().equals(user.getpassword())) {
    	// Authentication successful, return user details
//    		System.out.println(user.getpassword());
    		return true;
    	}
    	}
    	return false;	

    }
    @PostMapping("/api/support"	)
    public boolean supportteam(@RequestBody Support support) {
    	System.out.println(support.getName());
    	emailService.sendVerificationEmailforsupport(support.getName(),support.getEmail(),support.getMessage());
        System.out.println("Mail Send..");
    	return true;
    }
    
}