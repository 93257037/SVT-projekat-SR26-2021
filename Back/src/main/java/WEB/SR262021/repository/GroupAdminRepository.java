package com.ftn.SVT.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.SVT.model.GroupAdmin;

public interface GroupAdminRepository extends JpaRepository<GroupAdmin, Long> {

    GroupAdmin save(GroupAdmin groupAdmin);

}