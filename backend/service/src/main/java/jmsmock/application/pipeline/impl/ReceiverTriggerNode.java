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
import jmsmock.application.pipeline.Trigger;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
public class ReceiverTriggerNode extends AbstractNode implements Trigger, MessageListener {

    public static final String PARAMETER_RECEIVER_NAME = "receiver-name";

    private final Sinks.Many<Context> sink = Sinks.many().multicast().directBestEffort();

    private final EventService eventService;

    private final MessageConverter messageConverter;

    public ReceiverTriggerNode(NodeConfig nodeConfig, EventService eventService, MessageConverter messageConverter) {
        super(nodeConfig);
        this.eventService = eventService;
        this.messageConverter = messageConverter;
    }

    @Override
    public Flux<Context> getFlux() {
        return sink.asFlux();
    }

    @Override
    public void onMessage(Message message) {
        Context context = new Context();
        context.setAttribute(Context.INBOUND_MESSAGE, convertMessage(message));
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private org.springframework.messaging.Message<String> convertMessage(Message message) {
        return (org.springframework.messaging.Message<String>) messageConverter.fromMessage(message);
    }

}
