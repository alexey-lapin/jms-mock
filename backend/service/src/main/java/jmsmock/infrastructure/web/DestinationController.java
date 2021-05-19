package jmsmock.infrastructure.web;

import jmsmock.api.dto.BrowseDto;
import jmsmock.api.dto.DestinationDto;
import jmsmock.api.operation.DestinationOperations;
import jmsmock.service.DestinationBrowser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DestinationController implements DestinationOperations {

    private final DestinationBrowser destinationBrowser;
    private final ConversionService conversionService;

    @Override
    public List<DestinationDto> findAll() {
        return null;
    }

    @Override
    public void delete(String name) {

    }

    @Override
    public List<BrowseDto> browse(String name) {
        return destinationBrowser.browse(name).stream()
                .map(item -> conversionService.convert(item, BrowseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public int count(String name) {
        return destinationBrowser.count(name);
    }

}
