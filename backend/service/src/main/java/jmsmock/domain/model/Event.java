package jmsmock.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
public class Event {

    private UUID id;
    private ZonedDateTime createdAt;
    private EventType type;
    private String message;

    public static Event of(EventType eventType, String message) {
        return builder()
                .id(UUID.randomUUID())
                .createdAt(ZonedDateTime.now())
                .type(eventType)
                .message(message)
                .build();
    }

    public static Event info(String message) {
        return of(EventType.INFO, message);
    }

    public static Event warn(String message) {
        return of(EventType.WARN, message);
    }

    public static Event error(String message) {
        return of(EventType.ERROR, message);
    }

    public static Event ping() {
        return of(EventType.PING, "ping");
    }

}
