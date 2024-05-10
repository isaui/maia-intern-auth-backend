package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.dto.EmailVerificationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    @Override
    public void sendEmailVerification(EmailVerificationDTO emailVerificationSetting) {

        MimeMessage message = javaMailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("emailVerificationSetting", emailVerificationSetting);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailVerificationSetting.getRecipientEmail());
            helper.setSubject(emailVerificationSetting.getSubject());
            String htmlContent = templateEngine.process("email-template", context);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Terjadi kesalahan dalam mengirim email");
        }
    }

    @Override
    public boolean verifyEmail(String email) {
        return false;
    }
}

