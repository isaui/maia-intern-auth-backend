package com.isacitra.authentication.modules.authmodule.service;

import com.isacitra.authentication.modules.authmodule.model.dto.TokenEmailVerificationDTO;
import com.isacitra.authentication.modules.authmodule.model.dto.RegisterEmailVerificationDTO;
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
    public void sendEmailVerification(TokenEmailVerificationDTO emailVerificationSetting) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String emailTemplate = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0; padding: 0; width: 100%; height: 100%; background-color: #F3F4F6;\">\n" +
                    "<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"100%\">\n" +
                    "    <tr>\n" +
                    "        <td align=\"center\" valign=\"top\" style=\"padding: 20px 10px;\">\n" +
                    "            <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "                <tr>\n" +
                    "                    <td align=\"center\" bgcolor=\"#ffffff\" style=\"border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 40px;\">\n" +
                    "                        <h1 style=\"font-size: 24px; font-weight: bold; margin-bottom: 20px;\">Hai, <span>%s</span>.</h1>\n" +
                    "                        <p>%s</p>\n" +
                    "                        <button style=\"border: none; border-radius: 4px; padding: 10px 20px; font-size: 16px; font-weight: bold; cursor: pointer; margin-top: 20px;\">%s</button>\n" +
                    "                        <p><a href=\"%s\" style=\"color: #ffffff; text-decoration: none; display: inline-block; background-color: #3B82F6; padding: 10px 20px; border-radius: 4px; margin-top: 20px;\">%s</a></p>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>\n";
            helper.setTo(emailVerificationSetting.getRecipientEmail());
            helper.setSubject(emailVerificationSetting.getSubject());
            String htmlContent = String.format(emailTemplate, emailVerificationSetting.getName(), emailVerificationSetting.getContent(), emailVerificationSetting.getToken(), emailVerificationSetting.getRedirectLink());

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Terjadi kesalahan dalam mengirim email");
        }
    }

    @Override
    public void sendRegistrationEmailVerification(RegisterEmailVerificationDTO info) {
        System.out.println("SINI INFOKAN");
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(info.getRecipientEmail());
            helper.setSubject("Verify Your Email Address to Complete Registration");
            String emailTemplate = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>MAIA Email Verification</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p>Hello %s,</p>\n" +
                    "<p>Thank you for signing up with MAIA! We're thrilled to have you on board.</p>\n" +
                    "<p>To ensure the security of your account and access all the features, please verify your email address by clicking the link below:</p>\n" +
                    "<p><a href=\"%s\">Click here to verify your email</a></p>\n" +
                    "<p>If you have trouble clicking the link, please copy and paste it into your browser's address bar.</p>\n" +
                    "<p>Once your email is verified, you'll be ready to dive into MAIA exciting features.</p>\n" +
                    "<p>If you did not register with us, please ignore this email or contact our support team at <a href=\"mailto:support@maiadigital.id\">support@maiadigital.id</a>.</p>\n" +
                    "<p>Thank you for choosing MAIA!</p>\n" +
                    "<p>Best regards,<br>MAIA</p>\n" +
                    "</body>\n" +
                    "</html>\n";

            String htmlContent = String.format(emailTemplate, info.getName(), info.getRedirectLink());

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
            System.out.println("OIOIOI");

        } catch (MessagingException e) {
            System.out.println(e);
            throw new RuntimeException("Terjadi kesalahan dalam mengirim email");
        }
    }
}

