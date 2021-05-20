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
package jmsmock.infrastructure.web;

import jmsmock.api.dto.MockConfigDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import jmsmock.api.operation.MockOperations;
import jmsmock.application.pipeline.Context;
import jmsmock.domain.model.MockConfig;
import jmsmock.service.config.MockConfigService;
import jmsmock.service.MockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MockController implements MockOperations {

    private final MockConfigService mockConfigService;

    private final ConversionService conversionService;

    @Lazy
    private final MockManager mockManager;

    @Override
    public List<MockConfigDto> getAllMocks() {
        return mockConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, MockConfigDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MockConfigDto createMock(MockConfigDto command) {
        MockConfig config = conversionService.convert(command, MockConfig.class);
        MockConfig result = mockConfigService.createMock(config);
        return conversionService.convert(result, MockConfigDto.class);
    }

    @Override
    public MockConfigDto updateMock(String name, MockConfigDto command) {
        MockConfig config = conversionService.convert(command, MockConfig.class);
        MockConfig result = mockConfigService.updateMock(name, config);
        return conversionService.convert(result, MockConfigDto.class);
    }

    @Override
    public void deleteMock(String name) {
        mockConfigService.deleteMock(name);
    }

    @Override
    public MockConfigDto toggleMock(String name) {
        MockConfig result = mockConfigService.toggleMock(name);
        mockManager.toggleMock(name);
        return conversionService.convert(result, MockConfigDto.class);
    }

    @Override
    public void triggerMock(String name, @Valid TriggearbleSignalDto command) {
        Context context = conversionService.convert(command, Context.class);
        mockManager.triggerMock(name, context);
    }

}
