package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})

class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail은_Active_상태인_유저를_찾아올_수_있다(){
        //given
        String email = "kgh2120@naver.com";
        //when
        UserEntity sut = userService.getByEmail(email);
        //then
        assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
    } 
    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        String email = "kgh2120@gmail.com";
        //when //then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById은_Active_상태인_유저를_찾아올_수_있다(){
        //given
        long id = 10L;
        //when
        UserEntity sut = userService.getById(id);
        //then
        assertThat(sut.getId()).isEqualTo(id);
    }
    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        long id = 20L;
        //when //then
        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);


    }


    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("kgh2120@kakao.com")
                .address("Seoul")
                .nickname("hello@")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        //when
        UserEntity sut = userService.createUser(userCreateDto);
        //then
        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(sut.getCertificationCode()).isEqualTo("??");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다(){
        //given

        final String address = "Seoul22";
        final String nickname = "hello22";
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address(address)
                .nickname(nickname)
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        //when
        UserEntity sut = userService.updateUser(10L, userUpdateDto);
        //then
        assertThat(sut.getAddress()).isEqualTo(address);
        assertThat(sut.getNickname()).isEqualTo(nickname);
//        assertThat(sut.getCertificationCode()).isEqualTo("??");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
        //given
        //when
        userService.login(10L);
        //then
        UserEntity sut = userService.getById(10L);
        assertThat(sut.getLastLoginAt()).isGreaterThan(0L);
        //  assertThat(sut.getLastLoginAt()).isEqualTo(); FIXME
    }
    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        //given
        final String certificationCode = "aaaa";
        final long id = 20L;
        //when
        userService.verifyEmail(id, certificationCode);
        //then
        UserEntity sut = userService.getById(id);
        assertThat(sut.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        //given
        final String certificationCode = "bbbb";
        final long id = 20L;
        //when //then
        assertThatThrownBy(() -> userService.verifyEmail(id, certificationCode))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}