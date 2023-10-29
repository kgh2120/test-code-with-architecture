package com.example.demo.user.domain;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void UserCreate_객체로_생성할_수_있다(){

        //given
        UserCreate userCreate = UserCreate.builder()
                .email("kgh2120@kakao.com")
                .address("Seoul")
                .nickname("hello@")
                .build();

        //when
        User sut = User.from(userCreate, new TestUuidHolder("aaaa-aaaa-aaaa-aaaa"));

        //
        Assertions.assertThat(sut.getId()).isNull();
        Assertions.assertThat(sut.getEmail()).isEqualTo("kgh2120@kakao.com");
        Assertions.assertThat(sut.getAddress()).isEqualTo("Seoul");
        Assertions.assertThat(sut.getNickname()).isEqualTo("hello@");
        Assertions.assertThat(sut.getStatus()).isEqualTo(UserStatus.PENDING);
        Assertions.assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
    }

    @Test
    void UserUpdate_객체로_생성할_수_있다(){
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("newyork")
                .nickname("yorker")
                .build();
        User user = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa-aaaa-aaaa")
                .lastLoginAt(100L)
                .build();


        //when
       User sut = user.update(userUpdate);

        // then
        Assertions.assertThat(sut.getId()).isEqualTo(1L);
        Assertions.assertThat(sut.getEmail()).isEqualTo("foo@bar.com");
        Assertions.assertThat(sut.getAddress()).isEqualTo("newyork");
        Assertions.assertThat(sut.getNickname()).isEqualTo("yorker");
        Assertions.assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
        Assertions.assertThat(sut.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다(){
        // given
        User user = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa-aaaa-aaaa")
                .lastLoginAt(100L)
                .build();

        //when
        User sut = user.login(new TestClockHolder(200L));

        // then
        Assertions.assertThat(sut.getId()).isEqualTo(1L);
        Assertions.assertThat(sut.getEmail()).isEqualTo("foo@bar.com");
        Assertions.assertThat(sut.getAddress()).isEqualTo("seoul");
        Assertions.assertThat(sut.getNickname()).isEqualTo("foobar");
        Assertions.assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
        Assertions.assertThat(sut.getLastLoginAt()).isEqualTo(200L);
    }

    @Test
    void 인증_코드로_계정을_활성화_할_수_있다(){
        // given
        String certificationCode = "aaaa-aaaa-aaaa-aaaa";
        User user = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.PENDING)
                .certificationCode(certificationCode)
                .lastLoginAt(100L)
                .build();

        //when
        User sut = user.certificate(certificationCode);

        // then
        Assertions.assertThat(sut.getId()).isEqualTo(1L);
        Assertions.assertThat(sut.getEmail()).isEqualTo("foo@bar.com");
        Assertions.assertThat(sut.getAddress()).isEqualTo("seoul");
        Assertions.assertThat(sut.getNickname()).isEqualTo("foobar");
        Assertions.assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
        Assertions.assertThat(sut.getLastLoginAt()).isEqualTo(100L);
    }
    @Test
    void 잘못된_인증_코드로_계정을_활성화_하면_에러를_던진다(){
        // given
        String certificationCode = "aaaa-aaaa-aaaa-aaaa";
        User user = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.PENDING)
                .certificationCode(certificationCode)
                .lastLoginAt(100L)
                .build();

        //when
        assertThatThrownBy(() -> user.certificate(certificationCode+"1234"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }
}