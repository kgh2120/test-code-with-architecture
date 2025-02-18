package com.example.demo.user.service;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;


class UserServiceTest {


    private UserService userService;

    @BeforeEach
    void init() {
        FakeUserRepository userRepository = new FakeUserRepository();


        userService = UserService.builder()
                .userRepository(userRepository)
                .certificationService(new CertificationService(new FakeMailSender()))
                .uuidHolder(new TestUuidHolder("aaaa-aaaa-aaaa-aaaa"))
                .clockHolder(new TestClockHolder(200L))
                .build();

        userRepository.save(
                User.builder()
                        .id(10L)
                        .status(UserStatus.ACTIVE)
                        .address("Seoul")
                        .email("kgh2120@naver.com")
                        .nickname("kgh2120")
                        .certificationCode("aaaa-aaaa-aaaa-aaaa")
                        .lastLoginAt(0L)
                        .build()
        );

        userRepository.save(
                User.builder()
                        .id(20L)
                        .status(UserStatus.PENDING)
                        .address("Seoul")
                        .email("kgh2120@gmail.com")
                        .nickname("kgh2120")
                        .certificationCode("aaaa-aaaa-aaaa-aaaa")
                        .lastLoginAt(0L)
                        .build()
        );
    }

    @Test
    void getByEmail은_Active_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "kgh2120@naver.com";
        //when
        User sut = userService.getByEmail(email);
        //then
        assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "kgh2120@gmail.com";
        //when //then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById은_Active_상태인_유저를_찾아올_수_있다() {
        //given
        long id = 10L;
        //when
        User sut = userService.getById(id);
        //then
        assertThat(sut.getId()).isEqualTo(id);
    }

    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        long id = 20L;
        //when //then
        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);


    }


    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("kgh2120@kakao.com")
                .address("Seoul")
                .nickname("hello@")
                .build();
        //when
        User sut = userService.createUser(userCreate);
        //then
        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        //given

        final String address = "Seoul22";
        final String nickname = "hello22";
        UserUpdate userUpdate = UserUpdate.builder()
                .address(address)
                .nickname(nickname)
                .build();

        //when
        User sut = userService.updateUser(10L, userUpdate);
        //User
        assertThat(sut.getAddress()).isEqualTo(address);
        assertThat(sut.getNickname()).isEqualTo(nickname);
        assertThat(sut.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(10L);
        //then
        User sut = userService.getById(10L);
        assertThat(sut.getLastLoginAt()).isGreaterThan(0L);
        assertThat(sut.getLastLoginAt()).isEqualTo(200L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        final String certificationCode = "aaaa-aaaa-aaaa-aaaa";
        final long id = 20L;
        //when
        userService.verifyEmail(id, certificationCode);
        //then
        User sut = userService.getById(id);
        assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        //given
        final String certificationCode = "bbbb";
        final long id = 20L;
        //when //then
        assertThatThrownBy(() -> userService.verifyEmail(id, certificationCode))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}