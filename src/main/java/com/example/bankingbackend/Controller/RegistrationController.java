package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.EmailSenderService;
import com.example.bankingbackend.Entity.Accounts;
import com.example.bankingbackend.Entity.JwtRequest;
import com.example.bankingbackend.Entity.JwtResponse;
import com.example.bankingbackend.Entity.PasswordRequest;
import com.example.bankingbackend.Entity.Support;
import com.example.bankingbackend.Entity.UserInfo;
import com.example.bankingbackend.Exception.ResourceNotFoundException;
import com.example.bankingbackend.Exception.ValidationException;
import com.example.bankingbackend.Security.JwtService;
import com.example.bankingbackend.Security.UserInfoDetailsService;
import com.example.bankingbackend.Service.AccountService;
import com.example.bankingbackend.Service.UserInfoService;
import com.example.bankingbackend.repository.AccountsRepository;
import com.example.bankingbackend.repository.DebitRepository;
import com.example.bankingbackend.repository.UserInfoRepository;

@CrossOrigin("*")
@RestController
public class RegistrationController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserInfoDetailsService userDetailsService;

	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private PasswordEncoder encoder;

	private final EmailSenderService emailService;

	String customeridref;

	public RegistrationController(EmailSenderService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/api/user/password")
	public boolean updatePassword(@RequestBody PasswordRequest password) {
		System.out.println(password.getCustomerId() + " " + password.getPassword());
		
		UserInfo user=userInfoService.getdetailsbycustid(password.getCustomerId());
		//UserInfo user = userInfoRepository.findByCustomerId(password.getCustomerId());
//      System.out.println(user);
		if (user == null) {
			//return false;
			throw new ResourceNotFoundException("Cannot find this customer");
		}
		user.setpassword(encoder.encode(password.getPassword()));
		userInfoService.addDetails(user);
//		userInfoRepository.save(user);
		return true;
	}

	@PostMapping("/api/user/checkCustomerId/{customerId}")
	public boolean customeridinfo(@PathVariable String customerId) throws ResourceNotFoundException , ValidationException{
		List<Accounts> custid=accountService.getAccountByCustomerId(customerId);
		//List<Accounts> custid = accountsRepository.findByCustomerId(customerId);

		if (custid.isEmpty()) {
			//return false;
			throw new ResourceNotFoundException("Customer Id cannot be empty");
		} else {
			for (int i = 0; i < custid.size(); i++) {

				if (custid.get(i).getStatus().equals("Active")) {
//    				System.out.println(custid.get(i).getStatus()+" "+custid.get(i).getAccountNo());
					return true;
				}
			}
			//return false;
			throw new ValidationException("This account with customer id"+ customerId +"is not active");
		}
	}
	
	@PostMapping("/api/user/forgotpassword")
	public void forgetpasswordofuser(@RequestBody JwtRequest userInfo) {

		String verificationLink = "http://localhost:3000/user/passwordset";
		emailService.sendforgetpasswordEmail(userInfo.getEmailId(), verificationLink);
		System.out.println("Mail Send..");
		
	}

	@PostMapping("/api/user/register")
	public void registerUser(@RequestBody UserInfo userInfo) {
//    public void registerUser(@RequestBody UserInfo userInfo) {
		customeridref = userInfo.getCustomerId();
		UserInfo user= userInfoService.getdetailsbycustid(customeridref);
		//UserInfo user = userInfoRepository.findByCustomerId(customeridref);
		if (user == null) {
			userInfoService.addDetails(userInfo);
			//userInfoRepository.save(userInfo);
			String verificationLink = "http://localhost:3000/user/passwordset";
			emailService.sendVerificationEmail(userInfo.getEmailId(), verificationLink);
			System.out.println("Mail Send..");
		} else {
			System.out.println("Customer alrdy exists");
		}
	}

//	@PostMapping("/api/user/login")
//	public boolean loginUser(@RequestBody LoginForm loginForm) {
//
//		System.out.println(loginForm.getCustomerId());
//		System.out.println(loginForm.getEmailId());
//		System.out.println(loginForm.getPassword());
//		System.out.println(loginForm.getEmailId());
//		UserInfo user = userInfoRepository.findByCustomerIdOrEmailId(loginForm.getCustomerId(), loginForm.getEmailId());
//		if (user != null) {
//			// Verify password
//			if (loginForm.getPassword().equals(user.getpassword())) {
//				// Authentication successful, return user details
////    		System.out.println(user.getpassword());
//				return true;
//			}
//		}
//		return false;
//
//	}

	@PostMapping("/api/user/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmailId(), authenticationRequest.getPassword()));
			System.out.println("in login meth");
		} catch (UsernameNotFoundException e) {
//			LOGGER.error("User not found", e);
			return ResponseEntity.badRequest().body("User not found");
		} catch (BadCredentialsException e) {
//			LOGGER.error("Bad Credentials", e);
			return ResponseEntity.badRequest().body("Bad Credential");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmailId());
		String token = jwtService.generateToken(userDetails.getUsername());
//		LOGGER.info("Token generated for user {}", userDetails.getUsername());
		System.out.println(token);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@GetMapping("/api/user/login/{emailId}")
	public List<Accounts> getAccountDetails(@PathVariable String emailId) {
		List<Accounts> account = accountService.getAccountByEmailId(emailId);
		for(int i=0;i<account.size();i++)
			System.out.println(account.get(i));
		return account;
	}
	
	@PostMapping("/api/user/support")
	public boolean supportteam(@RequestBody Support support) {
		System.out.println(support.getName());
		emailService.sendVerificationEmailforsupport(support.getName(), support.getEmail(), support.getMessage());
		System.out.println("Mail Send..");
		return true;
	}

}