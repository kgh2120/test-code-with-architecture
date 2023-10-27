package com.example.demo.user.domain;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.UUID;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .email(email)
                .address(userUpdate.getAddress())
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .status(status)
                .nickname(userUpdate.getNickname())
                .id(id)
                .build();
    }
    public User login(){
        return User.builder()
                .email(email)
                .address(address)
                .certificationCode(certificationCode)
                .lastLoginAt(  Clock.systemUTC().millis())
                .status(status)
                .nickname(nickname)
                .id(id)
                .build();
    }

    public User certificate(String certificationCode){
        if (!certificationCode.equals(this.getCertificationCode())) {
            throw new CertificationCodeNotMatchedException();
        }
        return User.builder()
                .email(email)
                .address(address)
                .certificationCode(certificationCode)
                .lastLoginAt(lastLoginAt)
                .status(UserStatus.ACTIVE)
                .nickname(nickname)
                .id(id)
                .build();
    }
}
