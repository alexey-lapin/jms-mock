package jmsmock.service;

import jmsmock.api.dto.EventDto;
import jmsmock.domain.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RequiredArgsConstructor
@Slf4j
@Component
public class EventService {

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();
    private final ConversionService conversionService;

    public void emit(Event event) {
        EventDto eventDto = conversionService.convert(event, EventDto.class);
        List<SseEmitter> deadClients = new ArrayList<>();
        clients.forEach(client -> {
            try {
                client.send(eventDto, MediaType.APPLICATION_JSON);
            } catch (Exception ex) {
                log.info("dead client detected");
                deadClients.add(client);
            }
        });
        clients.removeAll(deadClients);
    }

    public void addClient(SseEmitter client) {
        client.onTimeout(() -> clients.remove(client));
        client.onCompletion(() -> clients.remove(client));
        clients.add(client);
    }

}
