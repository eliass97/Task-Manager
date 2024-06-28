package com.example.taskmanager.model.dto;

import com.example.taskmanager.model.persistance.BaseType;

public class TypeDTO {

    private String name;
    private String description;

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

    public static TypeDTO fromObject(BaseType baseType) {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setName(baseType.getName());
        typeDTO.setDescription(baseType.getDescription());
        return typeDTO;
    }
}
