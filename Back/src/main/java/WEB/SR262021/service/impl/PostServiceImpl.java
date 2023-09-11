package com.ftn.SVT.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.Post;
import com.ftn.SVT.model.Reaction;
import com.ftn.SVT.repository.PostRepository;
import com.ftn.SVT.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Override
	public List<Post> findPostsByGroup(Integer groupId) {
        List<Post> posts = new ArrayList<>();

        List<Post> allPosts =  postRepository.findAll();
        for (Post post : allPosts) {
        	  if (post.getGroup().getId().equals(groupId)) {
                  posts.add(post);
              }
        }
        return posts;
	}
	

    @Override
    public Post findPostById(Integer postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }
	
	@Override
    public List<Post> getAllPosts() {
        return postRepository.findAll(); 
    }

	@Override
	public Post createPost(Post post) {
		// TODO Auto-generated method stub
		return postRepository.save(post);
	}

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
	
    @Override
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }
}
