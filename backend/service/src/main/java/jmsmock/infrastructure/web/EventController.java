package jmsmock.infrastructure.web;

import jmsmock.service.EventService;
import jmsmock.service.ExtendedSseEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @GetMapping(path = "/events")
    public SseEmitter eventEmitter() {
        SseEmitter client = new ExtendedSseEmitter(Long.MAX_VALUE);
        eventService.addClient(client);
        return client;
    }

}
