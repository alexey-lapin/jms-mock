package jmsmock.infrastructure.web;

import jmsmock.api.dto.MockConfigDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import jmsmock.domain.model.MockConfig;
import jmsmock.application.pipeline.Context;
import jmsmock.service.MockConfigService;
import jmsmock.service.MockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MockController {

    private final MockConfigService mockConfigService;

    private final ConversionService conversionService;

    @Lazy
    private final MockManager mockManager;

    @GetMapping("/mocks")
    public List<MockConfigDto> getAllMocks() {
        return mockConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, MockConfigDto.class))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/mocks")
    public MockConfigDto createMock(@RequestBody MockConfigDto command) {
        MockConfig config = conversionService.convert(command, MockConfig.class);
        MockConfig result = mockConfigService.createMock(config);
        return conversionService.convert(result, MockConfigDto.class);
    }

    @PutMapping("/mocks/{name}")
    public MockConfigDto updateMock(@PathVariable String name, @RequestBody MockConfigDto command) {
        MockConfig config = conversionService.convert(command, MockConfig.class);
        MockConfig result = mockConfigService.updateMock(name, config);
        return conversionService.convert(result, MockConfigDto.class);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/mocks/{name}")
    public void deleteMock(@PathVariable String name) {
        mockConfigService.deleteMock(name);
    }

    @PostMapping("/mocks/{name}/trigger")
    public void triggerMock(@PathVariable String name, @Valid @RequestBody TriggearbleSignalDto command) {
        Context context = conversionService.convert(command, Context.class);
        mockManager.triggerMock(name, context);
    }

}
