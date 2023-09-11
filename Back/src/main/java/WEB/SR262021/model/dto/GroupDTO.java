package com.ftn.SVT.model.dto;

public class GroupDTO {

    private Integer id;
    private String name;
    private String description;
    private String groupAdmin;

    public GroupDTO() {
    }

    public GroupDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GroupDTO(Integer id, String name, String description, String groupAdmin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.groupAdmin = groupAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }
}