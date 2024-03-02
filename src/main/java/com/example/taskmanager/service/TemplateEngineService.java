package com.example.taskmanager.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateEngineService {

    private final TemplateEngine templateEngine;

    public TemplateEngineService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildMailBody(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mail_template", context);
    }
}
