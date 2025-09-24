package com.imperialnet.foodstore.users.infrastructure.email;

import com.imperialnet.foodstore.users.application.ports.out.EmailServicePort;
import com.imperialnet.foodstore.users.domain.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailEventListener {

    private final EmailServicePort emailService;


    @Async // 👈 se ejecuta en otro hilo
    @EventListener
    public void handleEmailEvent(EmailEvent event) {
        emailService.send(
                event.getTo(),
                event.getSubject(),
                event.getTemplateName(),
                event.getModel()
        );
    }
}
