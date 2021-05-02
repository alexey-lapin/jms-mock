package jmsmock.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;

@Configuration
public class JmsConfig {

    @Bean
    MessageConverter messageConverter() {
        return new MessagingMessageConverter();
    }

}
