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

import jmsmock.api.dto.MessageDto;
import jmsmock.api.dto.DestinationConfigDto;
import jmsmock.api.operation.DestinationOperations;
import jmsmock.domain.model.DestinationConfig;
import jmsmock.service.DestinationBrowser;
import jmsmock.service.config.DestinationConfigService;
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

    private final DestinationConfigService destinationConfigService;
    private final DestinationBrowser destinationBrowser;
    private final ConversionService conversionService;

    @Override
    public List<DestinationConfigDto> findAll() {
        return destinationConfigService.findAll().stream()
                .map(item -> conversionService.convert(item, DestinationConfigDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DestinationConfigDto createQueue(DestinationConfigDto command) {
        DestinationConfig config = conversionService.convert(command, DestinationConfig.class);
        DestinationConfig result = destinationConfigService.createDestination(config);
        return conversionService.convert(result, DestinationConfigDto.class);
    }

    @Override
    public DestinationConfigDto updateQueue(String name, DestinationConfigDto command) {
        DestinationConfig config = conversionService.convert(command, DestinationConfig.class);
        DestinationConfig result = destinationConfigService.updateDestination(name, config);
        return conversionService.convert(result, DestinationConfigDto.class);
    }

    @Override
    public void deleteQueue(String name) {
        destinationConfigService.deleteDestination(name);
    }

    @Override
    public List<MessageDto> browse(String name) {
        return destinationBrowser.browse(name).stream()
                .map(item -> conversionService.convert(item, MessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public int count(String name) {
        return destinationBrowser.count(name);
    }

    @Override
    public void purge(String name) {
        destinationBrowser.purge(name);
    }

}
