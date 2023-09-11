package com.ftn.SVT.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.SVT.model.Reaction;
import com.ftn.SVT.repository.PostRepository;
import com.ftn.SVT.repository.ReactionRepository;
import com.ftn.SVT.repository.UserRepository;
import com.ftn.SVT.service.ReactionService;

@Service
public class ReactionServiceImpl implements ReactionService {
	
	@Autowired
    private ReactionRepository reactionRepository;
    
	@Autowired
    private UserRepository userRepository;
    
	@Autowired
    private PostRepository postRepository;
	
	
    @Override
    public Reaction createReaction(Reaction reaction) {
    	reaction.setTimestamp(LocalDateTime.now());
    	Reaction createdReaction = reactionRepository.save(reaction);
        return createdReaction;
    }
    
    @Override
    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }
    
    @Override
    public Reaction findReactionById(Integer reactionId) {
        return reactionRepository.findById(reactionId).orElse(null);
    }

    @Override
    public void deleteReaction(Integer reactionId) {
        reactionRepository.deleteById(reactionId);
    }
	
    @Override
    public List<Reaction> getReactionsByPost(Integer postId) {
        List<Reaction> reactions = new ArrayList<>();

        // Retrieve reactions from the repository
        List<Reaction> allReactions = reactionRepository.findAll();

        // Iterate over each reaction
        for (Reaction reaction : allReactions) {
            // Check if the reaction's postId matches the provided postId
            if (reaction.getPost().getId().equals(postId)) {
                // Add the reaction to the list
                reactions.add(reaction);
            }
        }

        return reactions;
    }
}