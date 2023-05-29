package com.example.bankingbackend.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.repository.DebitRepository;
@Service
public class DebitService {
	@Autowired
	private DebitRepository debitRepository;
	
	public Debit getDebitDetails(Long cardNo) {
		Debit d = debitRepository.findByCardNo(cardNo);
		return d;
    }
	
	public boolean checkDebitExists(Long cardNo)
	{
		if(debitRepository.findByCardNo(cardNo)!= null)
			return true;
		else
			return false;
	}
	
}
