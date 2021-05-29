package jmsmock.infrastructure.converter;

import jmsmock.api.dto.MessageDto;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class MessageModelConverter implements ModelConverter<Message<?>, MessageDto> {

    @Override
    public MessageDto convert(Message<?> source) {
        return MessageDto.builder()
                .headers(source.getHeaders().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().toString(),
                                (a, b) -> b,
                                TreeMap::new)))
                .payload(source.getPayload().toString())
                .build();
    }

}
