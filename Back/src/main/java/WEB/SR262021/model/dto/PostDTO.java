package com.ftn.SVT.model.dto;

public class PostDTO {
	
	private Integer id;
	
	private String content;
	
    private String userName;
    
    private Integer groupId;
    
    
	public PostDTO() {
		super();
	}
	
	public PostDTO( Integer id, String content, String userName, Integer groupId) {
		super();
		this.id = id;
		this.content = content;
		this.userName = userName;
		this.groupId = groupId;
	}
	
	
	
	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Integer getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
    
}
