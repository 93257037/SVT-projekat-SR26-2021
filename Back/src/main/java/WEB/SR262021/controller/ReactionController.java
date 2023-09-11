package com.ftn.SVT.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.enums.ReactionType;
import com.ftn.SVT.model.Post;
import com.ftn.SVT.model.Reaction;
import com.ftn.SVT.model.User;
import com.ftn.SVT.model.dto.ReactionDTO;
import com.ftn.SVT.service.PostService;
import com.ftn.SVT.service.ReactionService;
import com.ftn.SVT.service.UserService;

@RestController
@RequestMapping("api/reactions")
@CrossOrigin(origins = "http://localhost:4200")
public class ReactionController {

	
	@Autowired
	ReactionService reactionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PostService postService;
	
	@PostMapping("/create")
	public ResponseEntity<Reaction> createReaction(@RequestBody ReactionDTO reactionDTO) {

	    User user = userService.findByUsername(reactionDTO.getUserName());
	    if (user == null) {
	        return ResponseEntity.badRequest().build();
	    }

	    Post post = postService.findPostById(reactionDTO.getPostId());
	    if (post == null) {
	        return ResponseEntity.badRequest().build();
	    }

	    List<Reaction> existingReactions = reactionService.getReactionsByPost(reactionDTO.getPostId());
	    Reaction existingReactionByUser = null;
	    for (Reaction existingReaction : existingReactions) {
	        if (existingReaction.getUser().getId().equals(user.getId())) {
	            existingReactionByUser = existingReaction;
	            break;
	        }
	    }

	    if (existingReactionByUser != null) {
	        // Delete the existing reaction by the same user for the same post
	        reactionService.deleteReaction(existingReactionByUser.getId());
	    }

	    Reaction createdReaction = new Reaction();
	    createdReaction.setReactionType(ReactionType.valueOf(reactionDTO.getReactionType()));
	    createdReaction.setTimestamp(LocalDateTime.now());
	    createdReaction.setUser(user);
	    createdReaction.setPost(post);

	    Reaction savedReaction = reactionService.createReaction(createdReaction);
	    if (savedReaction != null) {
	        return new ResponseEntity<>(savedReaction, HttpStatus.CREATED);
	    } else {
	        return ResponseEntity.badRequest().build();
	    }
	}
	
    @GetMapping("/all")
    public ResponseEntity<List<ReactionDTO>> getAllReactions() {
        List<Reaction> reactions = reactionService.getAllReactions();

        List<ReactionDTO> reactionDTOs = reactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reactionDTOs);
    }

    private ReactionDTO convertToDTO(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(reaction.getId());
        reactionDTO.setReactionType(reaction.getReactionType().toString());
        reactionDTO.setUserName(reaction.getUser().getUsername());
        reactionDTO.setPostId(reaction.getPost().getId());
        return reactionDTO;
    }

    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<String> deleteReaction(@PathVariable("reactionId") Integer reactionId) {
        Reaction reaction = reactionService.findReactionById(reactionId);
        if (reaction == null) {
            return ResponseEntity.notFound().build();
        }

        reactionService.deleteReaction(reactionId);
        return ResponseEntity.ok("Reaction deleted successfully");
    }
    
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deleteReactionsByPost(@PathVariable Integer postId) {
        List<Reaction> reactions = reactionService.getReactionsByPost(postId);

        if (!reactions.isEmpty()) {
            for (Reaction reaction : reactions) {
                reactionService.deleteReaction(reaction.getId());
            }
            return ResponseEntity.ok("Reactions deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}



