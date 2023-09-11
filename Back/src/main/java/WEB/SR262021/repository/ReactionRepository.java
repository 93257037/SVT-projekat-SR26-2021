package com.ftn.SVT.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.Reaction;

@RestController
public interface ReactionRepository extends JpaRepository<Reaction, Integer> { 

    List<Reaction> findByPostId(Integer postId);

}
