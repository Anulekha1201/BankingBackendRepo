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
import com.example.bankingbackend.Entity.Support;
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
    
    @PostMapping("/api/password")
    public boolean updatePassword( @RequestBody PasswordRequest password) {
      UserInfo user = UserInfoRepository.findByCustomerId(customeridref).orElse(null);

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
    	
    	UserInfoRepository.save(userInfo);
    	String verificationLink = "http://localhost:3000/passwordset";
        emailService.sendVerificationEmail(userInfo.getEmailId(), verificationLink);
        System.out.println("Mail Send..");
    }
    @PostMapping("/api/login")
    	
    public boolean loginUser(@RequestBody LoginForm loginForm) {
 
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

    }
    @PostMapping("/api/support")
    public boolean supportteam(@RequestBody Support support) {
    	System.out.println(support.getName());
    	emailService.sendVerificationEmailforsupport(support.getName(),support.getEmail(),support.getMessage());
        System.out.println("Mail Send..");
    	return true;
    }
    
}