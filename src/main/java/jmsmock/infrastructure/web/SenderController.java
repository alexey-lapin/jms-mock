package jmsmock.infrastructure.web;

import jmsmock.api.dto.SenderConfigDto;
import jmsmock.domain.model.SenderConfig;
import jmsmock.service.SenderConfigService;
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
public class SenderController {

    private final SenderConfigService senderConfigService;

    private final ConversionService conversionService;

    @GetMapping("/senders")
    public List<SenderConfigDto> getAllSenders() {
        return senderConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, SenderConfigDto.class))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/senders")
    public SenderConfigDto createSender(@RequestBody SenderConfigDto command) {
        SenderConfig config = conversionService.convert(command, SenderConfig.class);
        SenderConfig result = senderConfigService.createSender(config);
        return conversionService.convert(result, SenderConfigDto.class);
    }

    @PutMapping("/senders/{name}")
    public SenderConfigDto updateSender(@PathVariable String name, @RequestBody SenderConfigDto command) {
        SenderConfig config = conversionService.convert(command, SenderConfig.class);
        SenderConfig result = senderConfigService.updateSender(name, config);
        return conversionService.convert(result, SenderConfigDto.class);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/senders/{name}")
    public void deleteSender(@PathVariable String name) {
        senderConfigService.deleteSender(name);
    }

}
