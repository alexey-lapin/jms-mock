package jmsmock.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
public class EventDto {

    private UUID id;
    private ZonedDateTime createdAt;
    private String eventType;
    private String message;

}
