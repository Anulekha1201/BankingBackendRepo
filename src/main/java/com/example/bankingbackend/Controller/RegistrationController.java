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
import com.example.bankingbackend.Entity.PasswordRequest;
import com.example.bankingbackend.Entity.UserInfo;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.UserInfoRepository;
@CrossOrigin("*")

@RestController
@RequestMapping("/")
public class RegistrationController {
	@Autowired
	private UserInfoRepository UserInfoRepository;

    private final EmailSenderService emailService;
    
    String customeridref;
    public RegistrationController(EmailSenderService emailService) {
        this.emailService = emailService;
    }
    @PostMapping("/api/password")
    public boolean updatePassword( @RequestBody PasswordRequest password) {
      UserInfo user = UserInfoRepository.findByCustomerId(customeridref).orElse(null);
      System.out.println(customeridref);
      System.out.println(" "+password.getPassword());
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
}