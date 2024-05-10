package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.dto.EmailVerificationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailVerification(EmailVerificationDTO emailVerificationSetting) {
        // Membuat pesan email
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailVerificationSetting.getRecipientEmail());
            helper.setSubject(emailVerificationSetting.getSubject());
            String htmlContent = "<html>"
                    + "<head>"
                    + "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">"
                    + "</head>"
                    + "<body class=\"font-sans bg-gray-100\">"
                    + "<div class=\"max-w-2xl mx-auto py-8 px-4\">"
                    + "<div class=\"bg-white shadow-md rounded-lg px-8 py-6\">"
                    + "<h1 class=\"text-2xl font-bold mb-4\">Hai, " + emailVerificationSetting.getName() + ".</h1>"
                    + "<p>"+ emailVerificationSetting.getContent() +"</p>"
                    + "<p class=\"text-lg bg-indigo-200 rounded px-4 py-2\">" + emailVerificationSetting.getToken()+ "</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            // Mengirim pesan email
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

