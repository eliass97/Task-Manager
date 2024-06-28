package com.example.taskmanager.model.dto;

import java.time.LocalDateTime;

import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.UserProfile;

public class UserDataDTO {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String location;
    private String phone;
    private byte[] image;
    private TypeDTO jobTitle;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public TypeDTO getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(TypeDTO jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public static UserDataDTO fromObject(SystemUser systemUser) {
        UserProfile userProfile = systemUser.getProfile();
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(systemUser.getId());
        userDataDTO.setFirstName(userProfile.getFirstName());
        userDataDTO.setLastName(userProfile.getLastName());
        userDataDTO.setEmail(systemUser.getEmail());
        userDataDTO.setLocation(userProfile.getLocation());
        userDataDTO.setPhone(userProfile.getPhone());
        if (userProfile.getJobTitle() != null) {
            userDataDTO.setJobTitle(TypeDTO.fromObject(userProfile.getJobTitle()));
        }
        userDataDTO.setCreationDate(userProfile.getCreationDate());
        userDataDTO.setLastUpdateDate(userProfile.getLastUpdateDate());
        return userDataDTO;
    }
}
