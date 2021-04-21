package jmsmock.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.domain.Parameter;
import org.springframework.stereotype.Component;

import java.util.UUID;

public class ParameterConverter {

    @Component
    static class FromDto implements ModelConverter<ParameterDto, Parameter> {
        @Override
        public Parameter convert(ParameterDto source) {
            Parameter parameter = new Parameter();
            parameter.setId(UUID.randomUUID());
            parameter.setKey(source.getKey());
            parameter.setValue(source.getValue());
            return parameter;
        }
    }

    @Component
    static class ToDto implements ModelConverter<Parameter, ParameterDto> {
        @Override
        public ParameterDto convert(Parameter source) {
            return ParameterDto.builder()
                    .key(source.getKey())
                    .value(source.getValue())
                    .build();
        }
    }

}
