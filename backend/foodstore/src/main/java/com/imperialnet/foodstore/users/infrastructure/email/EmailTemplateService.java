package com.imperialnet.foodstore.users.infrastructure.email;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public EmailTemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, Object model) {
        Context context = new Context();
        if (model instanceof java.util.Map<?, ?> map) {
            map.forEach((k, v) -> context.setVariable(k.toString(), v));
        }
        return templateEngine.process(templateName, context);
    }
}
