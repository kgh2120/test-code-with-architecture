package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void PostCreate로_생성할_수_있다(){
        //given
        String certificationCode = UUID.randomUUID().toString();
        User writer = User.builder()
                .id(1L)
                .address("seoul")
                .email("foo@bar.com")
                .nickname("foobar")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode(certificationCode)
                .build();

        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foo bar!")
                .build();

        // when
        Post post = Post.from(postCreate,writer);

        // then
        assertThat(post.getContent()).isEqualTo("foo bar!");
        assertThat(post.getWriter().getEmail()).isEqualTo("foo@bar.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("foobar");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getCertificationCode()).isEqualTo(certificationCode);

    }

    @Test
    void PostUpdate로_수정할_수_있다(){

    }
}