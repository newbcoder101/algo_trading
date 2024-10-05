package com.doobie.trading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
     private JavaMailSender javaMailSender;

     public void sendVerificaionOtpMail(String mailId,String otp) throws MessagingException {
          MimeMessage mimeMessage=javaMailSender.createMimeMessage();

          MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,"utf-8");

          String subject ="verify otp";
          String body="Your Verificaion Otp is : "+otp;

          mimeMessageHelper.setSubject(subject);
          mimeMessageHelper.setText(body);
          mimeMessageHelper.setTo(mailId);

          try {
               javaMailSender.send(mimeMessage);

          }
          catch (MailException e)
          {
               throw new MailSendException(e.getMessage());
          }


      return;



     }
}
