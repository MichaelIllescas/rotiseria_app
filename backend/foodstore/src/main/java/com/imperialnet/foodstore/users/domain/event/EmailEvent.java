package com.imperialnet.foodstore.users.domain.event;

import java.util.Map;

/**
 * Evento genérico para envío de emails.
 */
public class EmailEvent {

    private final String to;
    private final String subject;
    private final String templateName;
    private final Map<String, Object> model;

    public EmailEvent(String to, String subject, String templateName, Map<String, Object> model) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
        this.model = model;
    }

    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getTemplateName() { return templateName; }
    public Map<String, Object> getModel() { return model; }
}
