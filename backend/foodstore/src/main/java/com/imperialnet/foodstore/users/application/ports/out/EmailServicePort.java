package com.imperialnet.foodstore.users.application.ports.out;

public interface EmailServicePort {
    void send(String to, String subject, String templateName, Object model);
}
