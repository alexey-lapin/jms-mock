package jmsmock.infrastructure.web;

import jmsmock.api.dto.SenderConfigDto;
import jmsmock.api.operation.SenderOperations;
import jmsmock.domain.model.SenderConfig;
import jmsmock.service.SenderConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SenderController implements SenderOperations {

    private final SenderConfigService senderConfigService;

    private final ConversionService conversionService;

    @Override
    public List<SenderConfigDto> getAllSenders() {
        return senderConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, SenderConfigDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SenderConfigDto createSender(SenderConfigDto command) {
        SenderConfig config = conversionService.convert(command, SenderConfig.class);
        SenderConfig result = senderConfigService.createSender(config);
        return conversionService.convert(result, SenderConfigDto.class);
    }

    @Override
    public SenderConfigDto updateSender(String name, SenderConfigDto command) {
        SenderConfig config = conversionService.convert(command, SenderConfig.class);
        SenderConfig result = senderConfigService.updateSender(name, config);
        return conversionService.convert(result, SenderConfigDto.class);
    }

    @Override
    public void deleteSender(String name) {
        senderConfigService.deleteSender(name);
    }

}
