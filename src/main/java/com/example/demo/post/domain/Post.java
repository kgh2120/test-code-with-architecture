package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@Getter
public class Post {

    private final Long id;
    private final String content;
    private final Long createdAt;
    private final Long modifiedAt;
    private final User writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, User writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static Post from(PostCreate postCreate, User writer) {
        return Post.builder()
                .writer(writer)
                .content(postCreate.getContent())
                .createdAt(Clock.systemUTC().millis())
                .build();
    }

    public Post update(PostUpdate postUpdate) {
        return Post.builder()
                .writer(writer)
                .content(postUpdate.getContent())
                .createdAt(createdAt)
                .modifiedAt(Clock.systemUTC().millis())
                .build();
    }
}
