package com.example.taskmanager.model.dto;

import java.time.LocalDateTime;

import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.UserProfile;

public class UserDataDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String imageURL;
    private TypeDTO jobTitle;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
        userDataDTO.setPhone(userProfile.getPhone());
        userDataDTO.setImageURL(userProfile.getImageURL());
        userDataDTO.setJobTitle(TypeDTO.fromObject(userProfile.getJobTitle()));
        userDataDTO.setCreationDate(userProfile.getCreationDate());
        userDataDTO.setLastName(userProfile.getLastName());
        return userDataDTO;
    }
}
