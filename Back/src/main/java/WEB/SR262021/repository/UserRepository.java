package com.ftn.SVT.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.User;

@RestController
public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findFirstByUsername(String username);

}

