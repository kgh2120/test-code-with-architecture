package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.CertificationService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다(){
        //given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(mailSender);


        //when
        long userId = 1L;
        String certificationUrl = "aaaa-aaaa-aaaa-aaa";
        String email = "foo@bar.com";
        certificationService.send(email, userId, certificationUrl);

        //then
        assertThat(mailSender.email).isEqualTo("foo@bar.com");
        assertThat(mailSender.title).isEqualTo("Please certify your email address");
        assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: " + "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationUrl);
    }

}