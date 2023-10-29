package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import java.time.Clock;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post로_응답_객체를_생성할_수_있다(){

        //given
        String certificationCode = UUID.randomUUID().toString();
        User writer = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.ACTIVE)
                .certificationCode(certificationCode)
                .lastLoginAt(100L)
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("foobar")
                .createdAt(Clock.systemUTC().millis())
                .writer(writer)
                .build();

        //when
        PostResponse sut = PostResponse.from(post);

        // then

        Assertions.assertThat(sut.getContent()).isEqualTo("foobar");
        Assertions.assertThat(sut.getId()).isEqualTo(1L);
        Assertions.assertThat(sut.getWriter().getEmail()).isEqualTo("foo@bar.com");
        Assertions.assertThat(sut.getWriter().getNickname()).isEqualTo("foobar");
        Assertions.assertThat(sut.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(sut.getWriter().getLastLoginAt()).isEqualTo(100L);


    }


}