package com.example.taskmanager.integration;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.mail.MailRequest;
import com.example.taskmanager.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JmsMailListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public JmsMailListener(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = CustomException.class)
    @JmsListener(destination = "${constants.account-activation-queue}")
    public void receiveAccountActivationRequest(String request) throws CustomException {
        MailRequest mailRequest;
        try {
            mailRequest = objectMapper.readValue(request, MailRequest.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Failed to deserialize message from broker!");
        }
        emailService.sendMail(mailRequest);
    }
}
