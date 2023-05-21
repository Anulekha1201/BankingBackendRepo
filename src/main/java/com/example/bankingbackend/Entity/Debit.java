package com.example.bankingbackend.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "debit")
public class Debit {

	@Column(name = "cardNo")
	private long cardNum;
	
	@Id
	@Column(name = "customerId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long accountNo;
	private long Pin;
	
	public Debit(long accountNo, long Pin) {
		super();
		this.accountNo = accountNo;
		this.Pin = Pin;
	}

	public long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}

	public long getPin() {
		return Pin;
	}

	public void setPin(long Pin) {
		this.Pin = Pin;
	}

	//@Column(name = "first_name")
	private String firstName;
	
	//@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "emailId")
	private String mail;
	
	@Column(name = "mobileNumber")
	private int num;
	
	@Column(name = "CVV")
	private int cvv;
	
	@Column(name = "validFrom")
	private static LocalDate validFrom;
	
	@Column(name = "validUpto")
	private LocalDate validUpto;
	
	public Debit() {}

	public Debit(long cardNum, long id, String firstName, String lastName, String mail, int num, int cvv, LocalDate validFrom,
			LocalDate validUpto) {
		super();
		this.cardNum = cardNum;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.num = num;
		this.cvv = cvv;
		Debit.validFrom = validFrom;
		this.validUpto = validUpto;
	}

	public long getCardNum() {
		return cardNum;
	}

	public void setCardNum(long cardNum) {
		this.cardNum = cardNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	public static LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		Debit.validFrom = validFrom;
	}

	public LocalDate getValidUpto() {
		return validUpto;
	}

	public void setValidUpto(LocalDate validUpto) {
		this.validUpto = validUpto;
	}
	
	
}
