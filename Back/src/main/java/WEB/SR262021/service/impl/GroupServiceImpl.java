package com.ftn.SVT.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.SVT.model.Group;
import com.ftn.SVT.repository.GroupRepository;
import com.ftn.SVT.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group findGroupById(Integer groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    @Override
    public Group createGroup(Group group) {
    	group.setCreationDate(LocalDateTime.now());
        Group createdGroup = groupRepository.save(group);
        return createdGroup;
    }

    @Override
    public Group updateGroup(Integer groupId, Group updatedGroup) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group != null) {
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
     
            group.setId(updatedGroup.getId());
            group.setGroupAdmin(updatedGroup.getGroupAdmin());

            return groupRepository.save(group);
        }

        return null;
    }

    @Override
    public void deleteGroup(Integer groupId) {
        groupRepository.deleteById(groupId);
    }

	@Override
	public List<Group> findByNameContainingIgnoreCase(String name) {
		return groupRepository.findByNameContainingIgnoreCase(name);
	}
}
