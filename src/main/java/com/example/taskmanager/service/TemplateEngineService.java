package com.example.taskmanager.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateEngineService {

    private static final String ACTIVATION_MAIL_TEMPLATE = "activation_mail_template";

    private final TemplateEngine templateEngine;

    public TemplateEngineService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildActivationMail(String activationUrl) {
        Context context = new Context();
        context.setVariable("activation_url", activationUrl);
        return templateEngine.process(ACTIVATION_MAIL_TEMPLATE, context);
    }
}
