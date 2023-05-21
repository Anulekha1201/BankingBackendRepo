package com.example.bankingbackend.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Debit;
import com.example.bankingbackend.repository.DebitRepository;

@RestController
public class DebitController {

	@Autowired
	private DebitRepository debitRepository;
	
	@GetMapping("/debitcards")
	public List<Debit> getList(){
		return debitRepository.findAll();
	}
	
	@GetMapping("/debit/{id}")
	public Optional<Debit> getById(@PathVariable long id) {
		return debitRepository.findById(id);
	}
	
	@PostMapping("/addNewDebit")
	public Debit saveDebit(@RequestBody Debit debit) {
		return debitRepository.save(debit);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public String deleteDebit(@PathVariable long id) {
		debitRepository.deleteById(id);
		return "Debit Card of Customer Id : "+id;
	}

}