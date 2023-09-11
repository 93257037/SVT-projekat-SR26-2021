package com.ftn.SVT.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.Post;
import com.ftn.SVT.model.dto.PostDTO;
import com.ftn.SVT.service.GroupService;
import com.ftn.SVT.service.PostService;
import com.ftn.SVT.service.UserService;

@RestController
@RequestMapping("api/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
	    // Kreiranje objekta Post i postavljanje vrednosti
	    Post createdPost = new Post();
	    createdPost.setContent(postDTO.getContent());
	    createdPost.setCreationDate(LocalDateTime.now());
	    createdPost.setGroup(groupService.findGroupById(postDTO.getGroupId()));
	    createdPost.setUser(userService.findByUsername(postDTO.getUserName()));
        createdPost.setCounter(0); // Postavljanje brojača na 0 prilikom kreiranja

	    // Kreiranje posta pozivom postService
	    Post savedPost = postService.createPost(createdPost);
	    
	    if (savedPost != null) {
	        // Konverzija objekta Post u PostDTO pre slanja odgovora
	        PostDTO responseDTO = new PostDTO();
	        responseDTO.setContent(savedPost.getContent());
	        responseDTO.setGroupId(savedPost.getGroup().getId());
	        responseDTO.setUserName(savedPost.getUser().getUsername());
	      
	        // Vraćanje uspešnog odgovora sa objektom PostDTO i statusom CREATED (201)
	        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	    } else {
	        // Vraćanje odgovora sa statusom INTERNAL_SERVER_ERROR (500) u slučaju greške
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	
	@GetMapping("/group/{groupId}")
	public ResponseEntity<List<PostDTO>> getPostsByGroup(@PathVariable Integer groupId) {
	    // Call the postService to retrieve posts by group
	    List<Post> posts = postService.findPostsByGroup(groupId);
	    List<PostDTO> dtos = new ArrayList<>();
	    for (Post post : posts) {
	        PostDTO dto = new PostDTO();
	        dto.setId(post.getId());
	        dto.setContent(post.getContent());
	        dto.setGroupId(post.getGroup().getId());
	        dto.setUserName(post.getUser().getUsername());
	        dtos.add(dto);
	    }
	    if (!dtos.isEmpty()) {
	        // If posts are found for the group, return them with a 200 OK status
	        return ResponseEntity.ok(dtos);
	    } else {
	        // If no posts are found for the group, return an empty response with a 404 Not Found status
	        return ResponseEntity.notFound().build();
	    }
	}

	
	@GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostDTO> dtos = new ArrayList<>();
        for (Post p: posts) {
            PostDTO dto = new PostDTO();
            dto.setId(p.getId());
            dto.setContent(p.getContent());
            dto.setGroupId(p.getGroup().getId());
            dto.setUserName(p.getUser().getUsername());
            dtos.add(dto);
        }
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(dtos);
        } 
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

	@PutMapping("/update/{postId}")
	public ResponseEntity<PostDTO> updatePost(@PathVariable Integer postId, @RequestBody String  postDTO) {
	    Post existingPost = postService.findPostById(postId);

	    if (existingPost != null) {
	        existingPost.setContent(postDTO);

	        Post updatedPost = postService.updatePost(existingPost);

	        if (updatedPost != null) {
	            PostDTO responseDTO = new PostDTO();
	            responseDTO.setId(updatedPost.getId());
	            responseDTO.setContent(updatedPost.getContent());
	            responseDTO.setUserName(updatedPost.getUser().getUsername());
	            responseDTO.setGroupId(updatedPost.getGroup().getId());

	            return ResponseEntity.ok(responseDTO);
	        }
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        Post existingPost = postService.findPostById(postId);

        if (existingPost != null) {
            postService.deletePost(postId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PutMapping("/update/counter/{postId}")
    public ResponseEntity<Void> increaseCounter(@PathVariable Integer postId) {
      Post existingPost = postService.findPostById(postId);

      if (existingPost != null) {
        existingPost.setCounter(existingPost.getCounter() + 1); // Increase the counter by 1

        Post updatedPost = postService.updatePost(existingPost);

        if (updatedPost != null) {
          return ResponseEntity.noContent().build();
        }
      }

      // If the post doesn't exist or the update fails, return a response with status 404 Not Found
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<Void> deletePostsByGroup(@PathVariable Integer groupId) {
        List<Post> posts = postService.findPostsByGroup(groupId);

        if (!posts.isEmpty()) {
            for (Post post : posts) {
                postService.deletePost(post.getId());
            }
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
