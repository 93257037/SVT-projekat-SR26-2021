package com.ftn.SVT.service;

import java.util.List;

import com.ftn.SVT.model.Group;

public interface GroupService {

    List<Group> getAllGroups();

    Group findGroupById(Integer groupId);

    Group createGroup(Group group);

    Group updateGroup(Integer groupId, Group updatedGroup);

    void deleteGroup(Integer groupId);

	List<Group> findByNameContainingIgnoreCase(String name);
}
