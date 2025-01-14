package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void User로_응답을_생성할_수_있다(){
        // given
        String certificationCode = UUID.randomUUID().toString();
        User user = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.ACTIVE)
                .certificationCode(certificationCode)
                .lastLoginAt(100L)
                .build();

        //when

        UserResponse sut = UserResponse.from(user);

        // then

        Assertions.assertThat(sut.getId()).isEqualTo(1L);
        Assertions.assertThat(sut.getEmail()).isEqualTo("foo@bar.com");
        Assertions.assertThat(sut.getNickname()).isEqualTo("foobar");
        Assertions.assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(sut.getLastLoginAt()).isEqualTo(100L);

    }

}