package com.ftn.SVT.service;

import java.util.List;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.User;
import com.ftn.SVT.model.dto.UserDTO;

public interface UserService {
	
    User findByUsername(String username);
    

    User createUser(UserDTO userDTO);
    
    List<User> findAll();
    
    void save(User user);


	List<Group> findGroupsByAdmin(Integer userId);

    
}
