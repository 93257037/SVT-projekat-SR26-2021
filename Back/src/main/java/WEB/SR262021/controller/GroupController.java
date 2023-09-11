package com.ftn.SVT.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.Post;
import com.ftn.SVT.model.Reaction;
import com.ftn.SVT.model.User;
import com.ftn.SVT.model.dto.GroupDTO;
import com.ftn.SVT.security.TokenUtils;
import com.ftn.SVT.service.GroupService;
import com.ftn.SVT.service.PostService;
import com.ftn.SVT.service.ReactionService;
import com.ftn.SVT.service.UserService;


@RestController
@RequestMapping("api/groups")
@CrossOrigin(origins = "http://localhost:4200")
public class GroupController {
	
		@Autowired
		GroupService groupService;

		@Autowired
		UserService userService;
		
		@Autowired
		PostService postService;
		
		@Autowired
		ReactionService reactionService;
		
	    @Autowired
	    UserDetailsService userDetailsService;

	    @Autowired
	    AuthenticationManager authenticationManager;

	    @Autowired
	    TokenUtils tokenUtils;
	    
	    @GetMapping()
	    public ResponseEntity<List<GroupDTO>> getAllGroups() {
	        List<Group> groups = groupService.getAllGroups();
	        List<GroupDTO> groupDTOs = new ArrayList<>();

	        for (Group group : groups) {
	            GroupDTO groupDTO = new GroupDTO();
	            groupDTO.setDescription(group.getDescription());
	            groupDTO.setName(group.getName());
	            groupDTO.setId(group.getId());
	            groupDTO.setGroupAdmin(group.getGroupAdmin().getUsername()); 
	            groupDTOs.add(groupDTO);
	        }

	        return ResponseEntity.ok(groupDTOs);
	    }


	    @PostMapping("/create/{username}")
	    public ResponseEntity<Group> createGroup(@RequestBody @Validated GroupDTO groupDto, @PathVariable String username) {
	        // Pronalaženje korisnika na osnovu korisničkog imena
	        User user = userService.findByUsername(username);
	        if (user == null) {
	            return ResponseEntity.badRequest().build();
	        }

	        Group createdGroup = new Group();
	        createdGroup.setDescription(groupDto.getDescription());
	        createdGroup.setName(groupDto.getName());
	        createdGroup.setCreationDate(LocalDateTime.now());
	        createdGroup.setGroupAdmin(user); // Set the retrieved user as the groupAdmin

	        Group savedGroup = groupService.createGroup(createdGroup);

	        if (savedGroup != null) {
	            return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
	        } else {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	    
	    @GetMapping("/{groupId}")
	    public ResponseEntity<GroupDTO> getGroup(@PathVariable Integer groupId) {
	        Group group = groupService.findGroupById(groupId);
	        GroupDTO dto = new GroupDTO();
	        dto.setDescription(group.getDescription());
	        dto.setId(groupId);
	        dto.setName(group.getName());
	        dto.setGroupAdmin(group.getGroupAdmin().getUsername()); // Set the groupAdmin username
	        if (group != null) { 
	            return ResponseEntity.ok(dto);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @PutMapping("/update/{groupId}")
	    public ResponseEntity<Group> updateGroup(@PathVariable Integer groupId, @RequestBody @Validated GroupDTO groupDto) {
	        Group existingGroup = groupService.findGroupById(groupId);

	        if (existingGroup != null) {
	            existingGroup.setName(groupDto.getName());
	            existingGroup.setDescription(groupDto.getDescription());

	            // Preserve the existing id and groupAdmin values
	            existingGroup.setId(existingGroup.getId());
	            existingGroup.setGroupAdmin(existingGroup.getGroupAdmin());

	            Group updatedGroup = groupService.updateGroup(groupId, existingGroup);

	            if (updatedGroup != null) {
	                return ResponseEntity.ok(updatedGroup);
	            } else {
	                return ResponseEntity.badRequest().build();
	            }
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }


	    @DeleteMapping("/delete/{groupId}")
	    public ResponseEntity<String> deleteGroup(@PathVariable Integer groupId) {
	        Group existingGroup = groupService.findGroupById(groupId);

	        if (existingGroup != null) {
	            List<Post> posts = postService.findPostsByGroup(groupId);
	            for (Post post : posts) {
	                // Delete reactions associated with the post
	                List<Reaction> reactions = reactionService.getReactionsByPost(post.getId());
	                for (Reaction reaction : reactions) {
	                    reactionService.deleteReaction(reaction.getId());
	                }
	                // Delete the post
	                postService.deletePost(post.getId());
	            }
	            // Delete the group
	            groupService.deleteGroup(groupId);

	            return ResponseEntity.ok("Group, posts, and associated reactions deleted successfully.");
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @GetMapping("/groups/{username}")
	    public ResponseEntity<List<GroupDTO>> getGroupsByAdmin(@PathVariable String username) {
	        // Pronalaženje korisnika na osnovu korisničkog imena
	        User user = userService.findByUsername(username);

	        if (user == null) {
	            // Ako korisnik ne postoji, vraćamo 404 Not Found odgovor
	            return ResponseEntity.notFound().build();
	        }

	        // Dohvatanje ID-ja korisnika
	        Integer userId = user.getId();

	        // Dohvatanje grupa koje ima korisnik kao vlasnik (groupAdmin)
	        List<Group> groups = userService.findGroupsByAdmin(userId);

	        // Konvertovanje List<Group> u List<GroupDTO>
	        List<GroupDTO> groupDTOs = groups.stream()
	                .map(group -> {
	                    GroupDTO groupDTO = new GroupDTO();
	                    groupDTO.setId(group.getId());
	                    groupDTO.setName(group.getName());
	                    groupDTO.setDescription(group.getDescription());
	                    groupDTO.setGroupAdmin(username);
	                    return groupDTO;
	                })
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(groupDTOs);
	    }


}