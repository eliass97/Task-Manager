package com.example.taskmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.RegistrationRequest;
import com.example.taskmanager.model.dto.UserDataDTO;
import com.example.taskmanager.model.enums.SystemAuthorityName;
import com.example.taskmanager.model.persistance.JobTitle;
import com.example.taskmanager.model.persistance.RegistrationToken;
import com.example.taskmanager.model.persistance.SystemAuthority;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.UserProfile;
import com.example.taskmanager.repository.SystemUserRepository;
import com.example.taskmanager.repository.UserProfileRepository;
import com.example.taskmanager.util.MailValidator;

@Service
public class SystemUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUserService.class);

    @Value("${constants.mail-confirmation-message}")
    private String mailConfirmationMessage;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationTokenService registrationTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private JobTitleService jobTitleService;

    public SystemUser getUserById(Long id) throws CustomException {
        return systemUserRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // TODO: Remove this since it was only used for testing
    public List<UserDataDTO> getAllUsers() {
        List<Object[]> usersMinimalInfo = systemUserRepository.getUsersMinimalInfo();
        List<UserDataDTO> userDataDTOs = new ArrayList<>();
        usersMinimalInfo.forEach(user -> {
            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setUserId((Long) user[0]);
            userDataDTO.setFirstName((String) user[1]);
            userDataDTO.setLastName((String) user[2]);
            userDataDTO.setEmail((String) user[3]);
            userDataDTOs.add(userDataDTO);
        });
        return userDataDTOs;
    }

    @Transactional
    public void registerUser(RegistrationRequest registrationRequest) throws CustomException {
        LOGGER.info("Received registration request: {}", registrationRequest);
        validateUserFields(registrationRequest);

        Optional<SystemUser> existingUserByUsername = systemUserRepository.findByUsername(registrationRequest.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Username is not available");
        }

        Optional<SystemUser> existingUserByEmail = systemUserRepository.findByEmail(registrationRequest.getEmail());

        SystemUser savedSystemUser;
        if (existingUserByEmail.isPresent()) {
            savedSystemUser = existingUserByEmail.get();
            if (savedSystemUser.isEnabled()) {
                throw new CustomException(HttpStatus.CONFLICT, "User with specified email already exists");
            }
        } else {
            SystemUser systemUser = buildNewUser(registrationRequest);
            savedSystemUser = systemUserRepository.save(systemUser);

            UserProfile userProfile = new UserProfile();
            userProfile.setSystemUser(savedSystemUser);
            userProfileRepository.save(userProfile);

            LOGGER.info("Created new system user with id {}", savedSystemUser.getId());
        }

        RegistrationToken registrationToken = registrationTokenService.createToken(savedSystemUser);
        emailService.sendMailToBroker(savedSystemUser.getEmail(), "Confirm your registration",
                String.format(mailConfirmationMessage, registrationToken.getToken()));
    }

    private static void validateUserFields(RegistrationRequest registrationRequest) throws CustomException {
        if (registrationRequest.getUsername() == null || registrationRequest.getUsername().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided username cannot not be empty");
        }
        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided email cannot be empty");
        }
        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided password cannot be empty");
        }
        if (!MailValidator.isMail(registrationRequest.getEmail())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided e-mail is invalid");
        }
    }

    private SystemUser buildNewUser(RegistrationRequest registrationRequest) {
        SystemUser systemUser = new SystemUser();
        systemUser.setUsername(registrationRequest.getUsername());
        systemUser.setEmail(registrationRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        systemUser.setPassword(encodedPassword);
        SystemAuthority systemAuthority = new SystemAuthority();
        systemAuthority.setSystemUser(systemUser);
        systemAuthority.setAuthority(SystemAuthorityName.CREATE_TASK.getValue());
        systemUser.getAuthorities().add(systemAuthority);
        return systemUser;
    }

    @Transactional
    public void activateUser(String token) throws CustomException {
        LOGGER.info("Received validation request for registration token: {}", token);
        RegistrationToken registrationToken = registrationTokenService.getByToken(token);

        boolean isExpired = LocalDateTime.now().isAfter(registrationToken.getExpirationDate());
        if (isExpired) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Registration token has expired");
        }

        SystemUser systemUser = registrationToken.getSystemUser();
        systemUser.setEnabled(true);
        LOGGER.info("Activating user: {}", systemUser.getUsername());
        systemUserRepository.save(systemUser);
    }

    @Transactional
    public UserDataDTO updateUserProfile(UserDataDTO userDataDTO) throws CustomException {
        SystemUser systemUser = systemUserRepository.findById(userDataDTO.getUserId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found"));

        UserProfile userProfile = systemUser.getProfile();
        if (userProfile.getLastUpdateDate().isAfter(userDataDTO.getLastUpdateDate())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User data are out-dated");
        }

        doBasicValidationChecks(userDataDTO);

        JobTitle jobTitle = jobTitleService.getJobTitleByName(userDataDTO.getJobTitle().getName());
        populateUserProfile(userProfile, userDataDTO, jobTitle);
        userProfileRepository.save(userProfile);

        return UserDataDTO.fromObject(systemUser);
    }

    private void doBasicValidationChecks(UserDataDTO userDataDTO) throws CustomException {
        if (userDataDTO.getFirstName() == null || userDataDTO.getFirstName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided first name is empty");
        }

        if (userDataDTO.getLastName() == null || userDataDTO.getLastName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided last name is empty");
        }

        if (userDataDTO.getJobTitle() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Provided job title is empty");
        }
    }

    private void populateUserProfile(UserProfile userProfile, UserDataDTO userDataDTO, JobTitle jobTitle) {
        userProfile.setFirstName(userDataDTO.getFirstName());
        userProfile.setLastName(userDataDTO.getLastName());
        userProfile.setPhone(userDataDTO.getPhone());
        userProfile.setImageURL(userDataDTO.getImageURL());
        if (jobTitle != null) {
            userProfile.setJobTitle(jobTitle);
        }
    }
}
