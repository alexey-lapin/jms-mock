/*
 * MIT License
 *
 * Copyright (c) 2021 - present Alexey Lapin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmsmock.infrastructure.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.api.dto.SenderConfigDto;
import jmsmock.domain.model.Parameter;
import jmsmock.domain.model.SenderConfig;
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

        @Lazy
        private final ConversionService conversionService;

        @Override
        public SenderConfig convert(SenderConfigDto source) {
            Set<Parameter> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, Parameter.class))
                    .collect(Collectors.toSet());

            return new SenderConfig(UUID.randomUUID(), source.getName(), parameters);
        }
    }

    @RequiredArgsConstructor
    @Component
    static class ToDto implements ModelConverter<SenderConfig, SenderConfigDto> {

        @Lazy
        private final ConversionService conversionService;

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
