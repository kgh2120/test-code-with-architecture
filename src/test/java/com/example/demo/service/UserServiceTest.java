package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})

class UserServiceTest {

    @Autowired
    UserService userService;

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
        long id = 1L;
        //when
        Optional<UserEntity> sut = userService.getById(id);
        //then
        assertThat(sut.isPresent()).isTrue();
    }
    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        long id = 2L;
        //when
        Optional<UserEntity> sut = userService.getById(id);
        //then
        assertThat(sut.isEmpty()).isTrue();


    }



}