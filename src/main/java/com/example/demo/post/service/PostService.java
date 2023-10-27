package com.example.demo.post.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.infrastructure.PostJpaRepository;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.infrastructure.UserEntity;

import java.time.Clock;

import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public Post createPost(PostCreate postCreate) {
        User writer = userService.getById(postCreate.getWriterId());
        Post post = Post.from(postCreate, writer);
        return postRepository.save(post);
    }

    public Post updatePost(long id, PostUpdate postUpdate) {
        Post post = getPostById(id);
        post = post.update(postUpdate);
        return postRepository.save(post);
    }
}