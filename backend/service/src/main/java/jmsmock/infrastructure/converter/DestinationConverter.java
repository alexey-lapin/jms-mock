package jmsmock.infrastructure.converter;

import jmsmock.api.dto.DestinationConfigDto;
import jmsmock.domain.model.DestinationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class DestinationConverter {

    @RequiredArgsConstructor
    @Component
    static class FromDto implements ModelConverter<DestinationConfigDto, DestinationConfig> {
        @Override
        public DestinationConfig convert(DestinationConfigDto source) {
            return new DestinationConfig(UUID.randomUUID(), source.getName());
        }
    }

    @RequiredArgsConstructor
    @Component
    static class ToDto implements ModelConverter<DestinationConfig, DestinationConfigDto> {
        @Override
        public DestinationConfigDto convert(DestinationConfig source) {
            return DestinationConfigDto.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .build();
        }
    }

}
