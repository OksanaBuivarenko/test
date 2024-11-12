package com.fintech.database.service.impl;

import com.fintech.database.security.AppUserDetails;
import com.fintech.database.service.MailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.login}")
    private String from;

    @Value("${mail.personal}")
    private String personal;

    @Override
    public void send() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setFrom(from, personal);
            messageHelper.setTo(getUsersEmail());
            messageHelper.setSubject("Код для изменения пароля");
            String content = getPasswordResetContent();
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Something went wrong with sending code on email.");
            log.error(e.getMessage());
        }
    }

    public String getUsersEmail() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            return userDetails.getEmail();
        }
        return null;
    }

    private String getPasswordResetContent() {
        return "<p>Доброго времени суток!</p>"
                + "<p>С вашего аккаунта был отправлен запрос на изменение текущего пароля.</p>"
                + "<p>Код для изменения пароля:</p>"
                + "<p>0000</p>"
                + "<br>"
                + "<p>Если вы не отправляли запрос на изменение пароля, то проигнорируйте данное сообщение.</p>";
    }
}