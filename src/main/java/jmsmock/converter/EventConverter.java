package jmsmock.converter;

import jmsmock.api.dto.EventDto;
import jmsmock.domain.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventConverter implements ModelConverter<Event, EventDto> {

    @Override
    public EventDto convert(Event source) {
        return EventDto.builder()
                .id(source.getId())
                .createdAt(source.getCreatedAt())
                .eventType(source.getType())
                .message(source.getMessage())
                .build();
    }

}
