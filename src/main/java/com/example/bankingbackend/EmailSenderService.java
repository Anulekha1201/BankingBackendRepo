package com.example.bankingbackend;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String recipientEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Email Verification");
        message.setText("Please click the following link to verify your email address: " + verificationLink);
        mailSender.send(message);
    }
    public void sendVerificationEmailforsetpin(String recipientEmail, Long debitcardno,Long pinno) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Debit Card Set/Reset Pin");
        message.setText("You have performed set/reset pin for ur debitcard of number: " + debitcardno + " with new pin no: "+pinno);
        mailSender.send(message);
    }
    public void sendVerificationEmailforBlockPin(String recipientEmail, Long debitcardno) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Debit Card Blocked");
        message.setText("You have Blocked the debit card of number " + debitcardno );
        mailSender.send(message);
    }

	public void sendVerificationEmailforsupport(String name, String email, String msg) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("anulekhaachanta7@gmail.com");
        message.setSubject("Support team Q/A");
        message.setText("A person named "+name+" of mail "+email+" has this query "+msg);
        mailSender.send(message);
		
	}
}
//
//import java.io.File;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
////import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//
//@Service
//public class EmailSenderService {
//	@Autowired
//	private JavaMailSender mailSender;
////	public void sendSimpleEmail(String toEmail,String body,String subject) {
////		SimpleMailMessage message =  new SimpleMailMessage();
////		
////		message.setFrom("anulekhaachanta7@gmail.com");
////		message.setTo(toEmail);
////		message.setText(body);
////		message.setSubject(subject);
////		
////		mailSender.send(message);
////		System.out.println("Mail send...");
////	}
//	public void sendEmailwithAttachment(String toEmail,String body,String subject,String attachment) throws MessagingException {
//		MimeMessage mimeMessage = mailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
//		
//		mimeMessageHelper.setFrom("anulekhaachanta7@gmail.com");
//		mimeMessageHelper.setTo(toEmail);
//		mimeMessageHelper.setText(body);
//		mimeMessageHelper.setSubject(subject);
//		
//		FileSystemResource fileSystem = new FileSystemResource(new File(attachment));
//		
//		mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
//		mailSender.send(mimeMessage);
//		System.out.println("Mail Send..");
//	}
//}
