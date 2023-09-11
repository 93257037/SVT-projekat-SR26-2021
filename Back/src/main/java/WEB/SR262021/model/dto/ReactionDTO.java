package com.ftn.SVT.model.dto;

import java.time.LocalDateTime;

import com.ftn.SVT.enums.ReactionType;
import com.ftn.SVT.model.Post;
import com.ftn.SVT.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionDTO {

    private Integer id;
    
	private String reactionType;
	    
    private String userName;

    private Integer postId;
    
    
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public String getReactionType() {
		return reactionType;
	}

	public void setReactionType(String reactionType) {
		this.reactionType = reactionType;
	}

    
    
   
    
    
}
