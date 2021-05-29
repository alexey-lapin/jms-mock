package jmsmock.infrastructure.converter;

import jmsmock.api.dto.MessageDto;
import jmsmock.domain.model.MessageHistoryItem;
import jmsmock.domain.model.MessageHistoryItemHeader;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class HistoryMessageConverter implements ModelConverter<MessageHistoryItem, MessageDto> {

    @Override
    public MessageDto convert(MessageHistoryItem source) {
        return MessageDto.builder()
                .headers(source.getHeaders().stream()
                        .collect(Collectors.toMap(
                                MessageHistoryItemHeader::getKey,
                                MessageHistoryItemHeader::getValue,
                                (a, b) -> b,
                                TreeMap::new)))
                .payload(source.getPayload())
                .build();
    }

}
