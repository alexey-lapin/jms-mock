package jmsmock.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.api.dto.ReceiverConfigDto;
import jmsmock.domain.Parameter;
import jmsmock.domain.ReceiverConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReceiverConfigConverter {

    @RequiredArgsConstructor
    @Component
    static class FromDto implements ModelConverter<ReceiverConfigDto, ReceiverConfig> {

        @Lazy
        private final ConversionService conversionService;

        @Override
        public ReceiverConfig convert(ReceiverConfigDto source) {
            Set<Parameter> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, Parameter.class))
                    .collect(Collectors.toSet());

            return new ReceiverConfig(UUID.randomUUID(), source.getName(), parameters);
        }
    }

    @RequiredArgsConstructor
    @Component
    static class ToDto implements ModelConverter<ReceiverConfig, ReceiverConfigDto> {

        @Lazy
        private final ConversionService conversionService;

        @Override
        public ReceiverConfigDto convert(ReceiverConfig source) {
            List<ParameterDto> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, ParameterDto.class))
                    .collect(Collectors.toList());

            return ReceiverConfigDto.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .parameters(parameters)
                    .build();
        }
    }

}
