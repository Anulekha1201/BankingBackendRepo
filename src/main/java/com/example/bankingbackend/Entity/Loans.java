package com.example.bankingbackend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "loans")
public class Loans {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_id")
	private Long loanId;
	
	@Column(name = "loan_type")
	private String loanType;
	
	@Size(min = 3,max = 10, message = "{firstname.invalid}")
	@NotBlank(message = "FirstName is mandatroy")
	@Column(name = "first_name")
	private String firstName;
	
	@Size(min = 3,max = 10, message = "{lastname.invalid}")
	@NotBlank(message = "LastName is mandatroy")
	@Column(name = "last_name")
	private String lastName;
	
	@Min(value = 1000000000000000L, message = "Card number should be 16 digits")
	@Max(value = 9999999999999999L, message = "Card number should be 16 digits")
	@Column(name = "card_no",unique=true)
	private Long cardNo;
	
	@Column(name = "total_loan_amount")
	private Long totalLoanAmt;
	
	private String tenure;
	
	@Column(name = "interest_rate")
	private float interestRate;
	
	@Column(name = "installment")
	private float installment;
	
	public Loans() {}


	public Loans(String loanType, String firstName, String lastName, Long cardNo, Long totalLoanAmt, String tenure,
			float interestRate, float installment) {
		super();
		this.loanType = loanType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cardNo = cardNo;
		this.totalLoanAmt = totalLoanAmt;
		this.tenure = tenure;
		this.interestRate = interestRate;
		this.installment = installment;
	}



	public Long getLoanId() {
		return loanId;
	}

	public synchronized void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getLoanType() {
		return loanType;
	}

	public synchronized void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getFirstName() {
		return firstName;
	}

	public synchronized void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public synchronized void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getcardNo() {
		return cardNo;
	}

	public synchronized void setcardNo(Long cardNo) {
		this.cardNo = cardNo;
	}

	public Long getTotalLoanAmt() {
		return totalLoanAmt;
	}

	public synchronized void setTotalLoanAmt(Long totalLoanAmt) {
		this.totalLoanAmt = totalLoanAmt;
	}

	public String getTenure() {
		return tenure;
	}

	public synchronized void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public synchronized void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public float getInstallment() {
		return installment;
	}

	public synchronized void setInstallment(float installment) {
		this.installment = installment;
	}
	
	

}
