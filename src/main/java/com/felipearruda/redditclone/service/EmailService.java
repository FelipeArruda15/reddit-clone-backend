package com.felipearruda.redditclone.service;

import com.felipearruda.redditclone.exception.SpringRedditException;
import com.felipearruda.redditclone.model.NotificationEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailContentBuilder contentBuilder;

    @Async
    public void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("springredditclone1@gmail.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(contentBuilder.build(notificationEmail.getBody()));
        };

        try {
            mailSender.send(mimeMessagePreparator);
            log.info("E-mail de verificação enviado");
        } catch (MailException e) {
            throw new SpringRedditException("Ocorreu um erro inesperado ao enviar e-mail para "
                    + notificationEmail.getRecipient() + e.getMessage() + e.getCause());
        }
    }
}
