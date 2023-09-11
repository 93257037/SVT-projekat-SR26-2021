package com.ftn.SVT.service;

import java.util.List;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.Post;

public interface PostService {

    List<Post> findPostsByGroup(Integer groupId);
    
    Post findPostById(Integer postId);

    List<Post> getAllPosts();
    
    Post createPost(Post post);

    Post updatePost(Post updatedPost);

    void deletePost(Integer postId);
}
