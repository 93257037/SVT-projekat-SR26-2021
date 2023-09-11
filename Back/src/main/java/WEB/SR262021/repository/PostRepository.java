package com.ftn.SVT.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.Post;

@RestController
public interface PostRepository extends JpaRepository<Post, Integer>{
	
	List<Post> findByGroup(Group group);

}
