package jmsmock.infrastructure.config;

import jmsmock.domain.model.Event;
import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SseConfig {

    @ConditionalOnProperty("app.events.ping.enabled")
    @Bean
    EventPingSender eventPingSender(EventService eventService) {
        return new EventPingSender(eventService);
    }

    @RequiredArgsConstructor
    static class EventPingSender {

        private final EventService eventService;

        // send ping event to keep connection alive
        @Scheduled(fixedRateString = "${app.events.ping.interval}")
        public void scheduleFixedDelayTask() {
            eventService.emit(Event.ping());
        }

    }

}
