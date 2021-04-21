package jmsmock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;

@Configuration
public class JmsConfig {

    @Bean
    MessageConverter messageConverter() {
        return new MessagingMessageConverter();
    }

    @Bean
    ScriptEvaluator groovyScriptEvaluator() {
        return new GroovyScriptEvaluator();
    }

}
