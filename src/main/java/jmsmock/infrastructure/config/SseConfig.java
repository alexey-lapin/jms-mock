package jmsmock.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Configuration
public class SseConfig {

    @Bean
    SseEmitter eventSseEmitter() {
        return new SseEmitter();
    }

}
