package com.ftn.SVT.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ftn.SVT.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id                               
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Integer id;
	
	@Column(unique = true, nullable = false)
    private String username;
    
	@Column(nullable = false)
    private String password;
    
	@Column(nullable = false)
    private String email;
    
	@Column
    private LocalDateTime lastLogin;

	@Column(nullable = false)
    private String firstName;
    
	@Column(nullable = false)
    private String lastName;
	
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Post> postList = new HashSet<Post>();
    

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<GroupAdmin> groupAdminList = new HashSet<GroupAdmin>();
    
    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public Set<Post> getPostList() {
		return postList;
	}

	public void setPostList(Set<Post> postList) {
		this.postList = postList;
	}

	public Set<GroupAdmin> getGroupAdminList() {
		return groupAdminList;
	}

	public void setGroupAdminList(Set<GroupAdmin> groupAdminList) {
		this.groupAdminList = groupAdminList;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
    





}
