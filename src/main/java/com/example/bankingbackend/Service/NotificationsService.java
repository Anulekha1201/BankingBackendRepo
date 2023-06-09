package com.example.bankingbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.bankingbackend.Entity.Notifications;
import com.example.bankingbackend.repository.NotificationsRepository;

@Service
public class NotificationsService {
	@Autowired
	private NotificationsRepository notificationsRepository;
	
	public void saveAccounts(Notifications notification)
	{
		notificationsRepository.save(notification);		
	}
	public Notifications getnotificationsDetails(long cardNo, String notificationType) {
		Notifications n = notificationsRepository.findByCardNoAndNotificationType(cardNo,notificationType);
		return n;
    }
}
