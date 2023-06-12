package com.example.bankingbackend.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private JwtAuthFilter authFilter;

	@Autowired
	private JwtAuthEntry authenticationEntryPoint;

	@Bean
	UserDetailsService userDetailsService() {
		return new UserInfoDetailsService();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		LOGGER.info("Configuring security filter chain...");
		return http.csrf().disable().authorizeHttpRequests()
				.requestMatchers("/api/user/notifications","/api/user/transactionHistory/{accountNo}",
						"/api/admindashboard/DebitapprovedHistory","/api/admindashboard/updatestatus/{cardNo}", "/api/admindashboard",

						"/api/user/login", "/api/user/password", "/api/user/checkCustomerId/{customerId}", "/api/user/register",
						"/api/addLoans","/api/admindashboard/DebitapprovalsHistory","/api/user/**","/api/admindashboard/**","/api/admin/**",

						"/login", "/api/user/password", "/api/user/checkCustomerId/{customerId}", "/api/user/register",
						"/api/addLoans","/api/admindashboard/DebitapprovalsHistory","/api/user/**","/api/admindashboard/**",
				"/api/admin/**","/getcredit","/api/user/unblockcreditcard","/api/user/blockcreditcard","/api/user/setorresetpinforcredit","/api/user/blockcard"
						,"api/admin/updateAccount/{id}","/api/admin/viewAccount","api/user/register","/login", "/api/user/password",
				"api/admin/addAccount","api/admin/getAccountById/{id}","api/admin/deleteAccount/{id}","api/admin/updateAccount/{id}","api/user/register","/login", "/api/user/password","/api/admindashboard/**","/api/admin/**","api/admin/addAccount","api/admin/getAccountById/{id}","api/admin/deleteAccount/{id}","/api/user/creditaccountnocheck/{accountNo}"
				,"/api/admindashboard/CreditApprovedHisory","/api/admindashboard/CreditapprovalsHistory","/api/admin/getAccountById/{id}")

				.permitAll().anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		LOGGER.info("Creating authentication manager...");
		return config.getAuthenticationManager();
	}

}
