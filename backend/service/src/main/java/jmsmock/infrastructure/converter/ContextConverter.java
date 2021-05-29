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
package jmsmock.infrastructure.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import jmsmock.application.pipeline.Context;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContextConverter implements ModelConverter<TriggearbleSignalDto, Context> {

    @Override
    public Context convert(TriggearbleSignalDto source) {
        Map<String, String> parameters = source.getParameters().stream()
                .collect(Collectors.toMap(ParameterDto::getKey, ParameterDto::getValue));

        String payload = parameters.remove("payload");

        Context context = new Context();

        if (payload != null) {
            MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
            messageHeaderAccessor.copyHeaders(parameters);
            Message<String> message = MessageBuilder.createMessage(payload, messageHeaderAccessor.getMessageHeaders());
            context.setAttribute(Context.OUTBOUND_MESSAGE, message);
        }

        return context;
    }

}
