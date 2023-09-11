package com.ftn.SVT.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ftn.SVT.model.GroupAdmin;
import com.ftn.SVT.repository.GroupAdminRepository;
import com.ftn.SVT.service.GroupAdminService;


public class GroupAdminServiceImpl implements GroupAdminService {

	  @Autowired
	   private GroupAdminRepository groupAdminRepository;
	  
	  @Override
	    public void save(GroupAdmin groupAdmin){
	        groupAdminRepository.save(groupAdmin);
	    }
	  
}
