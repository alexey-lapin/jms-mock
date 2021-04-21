package jmsmock.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.api.dto.SenderConfigDto;
import jmsmock.domain.Parameter;
import jmsmock.domain.SenderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SenderConfigConverter {

    @RequiredArgsConstructor
    @Component
    static class FromDto implements ModelConverter<SenderConfigDto, SenderConfig> {

        private final @Lazy
        ConversionService conversionService;

        @Override
        public SenderConfig convert(SenderConfigDto source) {
            Set<Parameter> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, Parameter.class))
                    .collect(Collectors.toSet());

            SenderConfig senderConfig = new SenderConfig(UUID.randomUUID(), source.getName(), parameters);
//            senderConfig.setId(UUID.randomUUID());
//            senderConfig.setName(source.getName());
//            senderConfig.setParameters(parameters);
            return senderConfig;
        }
    }

    @RequiredArgsConstructor
    @Component
    static class ToDto implements ModelConverter<SenderConfig, SenderConfigDto> {

        private final @Lazy
        ConversionService conversionService;

        @Override
        public SenderConfigDto convert(SenderConfig source) {
            List<ParameterDto> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, ParameterDto.class))
                    .collect(Collectors.toList());

            return SenderConfigDto.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .parameters(parameters)
                    .build();
        }
    }

}
