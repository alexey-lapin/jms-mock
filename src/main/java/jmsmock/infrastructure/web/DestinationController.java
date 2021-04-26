package jmsmock.infrastructure.web;

import jmsmock.api.dto.BrowseDto;
import jmsmock.api.dto.DestinationDto;
import jmsmock.service.DestinationBrowser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DestinationController {

    private final DestinationBrowser destinationBrowser;
    private final ConversionService conversionService;

    @GetMapping("/queues")
    public List<DestinationDto> findAll() {
        return null;
    }

    @DeleteMapping("/queues/{name}")
    public void delete(String name) {

    }

    @GetMapping("/queues/{name}/browse")
    public List<BrowseDto> browse(@PathVariable String name) {
        return destinationBrowser.browse(name).stream()
                .map(item -> conversionService.convert(item, BrowseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/queues/{name}/count")
    public int count(@PathVariable String name) {
        return destinationBrowser.count(name);
    }

}
