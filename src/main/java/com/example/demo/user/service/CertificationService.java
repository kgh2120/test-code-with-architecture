package com.example.demo.user.service;

import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CertificationService {

    private final MailSender mailSender;
    public void send(String email, long userId, String certificationUrl) {
        certificationUrl = generateCertificationUrl(userId, certificationUrl);
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address: " + certificationUrl;
        mailSender.send(email,title,content);

    }
    private String generateCertificationUrl(long userId, String certificationUrl) {
        return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationUrl;
    }
}
