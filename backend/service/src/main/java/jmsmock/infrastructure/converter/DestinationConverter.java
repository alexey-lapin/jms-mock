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
