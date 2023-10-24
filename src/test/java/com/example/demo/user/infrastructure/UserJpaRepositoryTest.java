package com.example.demo.user.infrastructure;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {

        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1L,
                UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_리턴한다() throws Exception {

        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1L,
                UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {

        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("kgh2120@naver.com",
                UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_리턴한다() throws Exception {

        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("kgh2120@naver.com",
                UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }
}
