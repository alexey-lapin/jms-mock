package jmsmock.infrastructure.web;

import jmsmock.api.dto.ReceiverConfigDto;
import jmsmock.api.operation.ReceiverOperations;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.service.JmsListenerService;
import jmsmock.service.ReceiverConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReceiverController implements ReceiverOperations {

    private final ReceiverConfigService receiverConfigService;

    private final ConversionService conversionService;

    private final JmsListenerService jmsListenerService;

    @Override
    public List<ReceiverConfigDto> getAllReceivers() {
        return receiverConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, ReceiverConfigDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReceiverConfigDto createReceiver(ReceiverConfigDto command) {
        ReceiverConfig config = conversionService.convert(command, ReceiverConfig.class);
        ReceiverConfig result = receiverConfigService.createReceiver(config);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

    @Override
    public ReceiverConfigDto updateReceiver(String name, ReceiverConfigDto command) {
        ReceiverConfig config = conversionService.convert(command, ReceiverConfig.class);
        ReceiverConfig result = receiverConfigService.updateReceiver(name, config);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

    @Override
    public void deleteReceiver(String name) {
        receiverConfigService.deleteReceiver(name);
    }

    @Override
    public ReceiverConfigDto toggleReceiver(String name) {
        ReceiverConfig result = receiverConfigService.toggle(name);
        jmsListenerService.toggle(name);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

}
