package com.ftn.SVT.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ftn.SVT.enums.RoleEnum;
import com.ftn.SVT.model.Group;
import com.ftn.SVT.model.User;
import com.ftn.SVT.model.dto.UserDTO;
import com.ftn.SVT.repository.GroupRepository;
import com.ftn.SVT.repository.UserRepository;
import com.ftn.SVT.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }
    


    @Override
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(RoleEnum.USER);
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser = userRepository.save(newUser);

        return newUser;
    }
    
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public void save(User user){
        userRepository.save(user);
    }
    
    @Override
    public List<Group> findGroupsByAdmin(Integer userId) {
        return groupRepository.findAll().stream()
                .filter(group -> group.getGroupAdmin().getId().equals(userId))
                .collect(Collectors.toList());
    }
}