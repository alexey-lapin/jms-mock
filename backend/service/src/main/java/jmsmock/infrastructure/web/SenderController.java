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

import jmsmock.api.dto.SenderConfigDto;
import jmsmock.api.operation.SenderOperations;
import jmsmock.domain.model.SenderConfig;
import jmsmock.service.config.SenderConfigService;
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
