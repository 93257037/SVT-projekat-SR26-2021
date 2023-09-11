package com.ftn.SVT.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.Group;

@RestController
public interface GroupRepository extends JpaRepository<Group, Integer> {
	
	List<Group> findByNameContainingIgnoreCase(String name);

}

