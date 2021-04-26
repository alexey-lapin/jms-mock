package jmsmock.infrastructure.web;

import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EventController {

//    private final SseEmitter eventSsEmitter;
    private final EventService eventService;

    @GetMapping(path = "/events-sse")
    public SseEmitter eventEmitter() {
        SseEmitter client = new SseEmitter(Long.MAX_VALUE);
        eventService.addClient(client);
        return client;
    }

}
