package jmsmock.api.dto;

import jmsmock.domain.model.EventType;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
public class EventDto {

    private UUID id;
    private ZonedDateTime createdAt;
    private EventType eventType;
    private String message;

}
