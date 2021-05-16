package jmsmock.infrastructure.web;

import jmsmock.api.dto.ReceiverConfigDto;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.service.JmsListenerService;
import jmsmock.service.ReceiverConfigService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReceiverController {

    private final ReceiverConfigService receiverConfigService;

    private final ConversionService conversionService;

    private final JmsListenerService jmsListenerService;

    @GetMapping("/receivers")
    public List<ReceiverConfigDto> getAllReceivers() {
        return receiverConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, ReceiverConfigDto.class))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/receivers")
    public ReceiverConfigDto createReceiver(@RequestBody ReceiverConfigDto command) {
        ReceiverConfig config = conversionService.convert(command, ReceiverConfig.class);
        ReceiverConfig result = receiverConfigService.createReceiver(config);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

    @PutMapping("/receivers/{name}")
    public ReceiverConfigDto updateReceiver(@PathVariable String name,
                                            @RequestBody ReceiverConfigDto command) {
        ReceiverConfig config = conversionService.convert(command, ReceiverConfig.class);
        ReceiverConfig result = receiverConfigService.updateReceiver(name, config);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/receivers/{name}")
    public void deleteReceiver(@PathVariable String name) {
        receiverConfigService.deleteReceiver(name);
    }

    @PostMapping("/receivers/{name}/toggle")
    public ReceiverConfigDto toggleReceiver(@PathVariable String name) {
        ReceiverConfig result = receiverConfigService.toggle(name);
        jmsListenerService.toggle(name);
        return conversionService.convert(result, ReceiverConfigDto.class);
    }

}
