package com.example.bankingbackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingbackend.Entity.Notifications;
import com.example.bankingbackend.Service.NotificationsService;
import com.example.bankingbackend.repository.NotificationsRepository;



@CrossOrigin("*")
@RestController
public class NotificationsController {

	@Autowired
	private NotificationsService notificationsService;
	
	@Autowired
	  private NotificationsRepository notificationsRepository;

	  @GetMapping("/api/user/notifications")
	  public List<Notifications> getNotifications() {
	    return notificationsRepository.findAll();
	  }
	
}
