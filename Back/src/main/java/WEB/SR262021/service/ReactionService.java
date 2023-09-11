package com.ftn.SVT.service;

import java.util.List;

import com.ftn.SVT.model.Reaction;
import com.ftn.SVT.model.dto.ReactionDTO;

public interface ReactionService {

	Reaction createReaction(Reaction reaction);

	List<Reaction> getAllReactions();

	Reaction findReactionById(Integer reactionId);

	void deleteReaction(Integer reactionId);
	
    List<Reaction> getReactionsByPost(Integer postId);


	
}
