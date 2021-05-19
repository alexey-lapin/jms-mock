package jmsmock.infrastructure.web;

import jmsmock.api.dto.MockConfigDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import jmsmock.api.operation.MockOperations;
import jmsmock.application.pipeline.Context;
import jmsmock.domain.model.MockConfig;
import jmsmock.service.MockConfigService;
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
