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
package jmsmock.application.pipeline.impl;

import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import jmsmock.domain.model.Event;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
public class SenderHandlerNode extends AbstractNode implements Handler {

    public static final String PARAMETER_SENDER_NAME = "sender-name";

    private final EventService eventService;

    private final String type;

    private final JmsOperations jmsOperations;
    private final RabbitOperations rabbitOperations;

    private final String destination;

    public SenderHandlerNode(NodeConfig nodeConfig,
                             EventService eventService,
                             JmsOperations jmsOperations,
                             RabbitOperations rabbitOperations,
                             String type,
                             String destination) {
        super(nodeConfig);
        this.eventService = eventService;
        this.jmsOperations = jmsOperations;
        this.rabbitOperations = rabbitOperations;
        this.type = type;
        this.destination = destination;
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            Optional<Message<String>> outboundMessage = context.getAttribute(Context.OUTBOUND_MESSAGE);
            if (outboundMessage.isPresent()) {
                try {
                    Message<String> outbound = outboundMessage.get();
                    log.info(outbound.toString());
                    if ("rabbit".equals(type)) {
                        rabbitOperations.convertAndSend("exchange", "rkey", outbound);
//                        rabbitOperations.convertAndSend(outbound);
                    } else {
                        jmsOperations.convertAndSend(destination, outbound);
                    }
                } catch (Exception ex) {
                    log.error("failed to send message", ex);
                }
            } else {
                String event = String.format("mock [name=%s] sender [name=%s] skips message sending",
                        context.getAttribute(Context.MOCK).get().getMockConfig().getName(),
                        getNodeConfig().getParameter(PARAMETER_SENDER_NAME).get());
                eventService.emit(Event.warn(event));
                log.warn(event);
            }
            return context;
        });
    }

}
