package jmsmock.application.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.Message;

@AllArgsConstructor
@Getter
public class TemplateEvaluationContext {

    private final Message inbound;

}
