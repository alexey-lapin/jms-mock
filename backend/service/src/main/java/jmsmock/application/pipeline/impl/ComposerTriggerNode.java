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
import jmsmock.application.pipeline.Triggerable;
import jmsmock.domain.model.MessageHistoryItem;
import jmsmock.domain.model.MessageHistoryItemHeader;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.repository.MessageHistoryItemRepository;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ComposerTriggerNode extends AbstractNode implements Trigger, Triggerable {

    private final Sinks.Many<Context> sink = Sinks.many().multicast().directBestEffort();

    private final EventService eventService;
    private final MessageHistoryItemRepository messageHistoryItemRepository;

    private final String mockName;

    public ComposerTriggerNode(MockConfig mockConfig,
                               NodeConfig nodeConfig,
                               EventService eventService,
                               MessageHistoryItemRepository messageHistoryItemRepository) {
        super(nodeConfig);
        this.eventService = eventService;
        this.messageHistoryItemRepository = messageHistoryItemRepository;
        this.mockName = mockConfig.getName();
    }

    @Override
    public Flux<Context> getFlux() {
        return sink.asFlux();
    }

    @Override
    public void trigger(Context context) {
        saveHistory(context);
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    private void saveHistory(Context context) {
        context.getAttribute(Context.OUTBOUND_MESSAGE).ifPresent(inbound -> {
            Set<MessageHistoryItemHeader> collect = inbound.getHeaders().entrySet().stream()
                    .map(item -> new MessageHistoryItemHeader(UUID.randomUUID(), item.getKey(), item.getValue().toString()))
                    .collect(Collectors.toSet());
            MessageHistoryItem messageHistoryItem = new MessageHistoryItem(
                    UUID.randomUUID(),
                    inbound.getPayload(),
                    ZonedDateTime.now(),
                    mockName,
                    new TreeSet<>(collect));
            try {
                messageHistoryItemRepository.save(messageHistoryItem);
            } catch (Exception ex) {
                log.error("failed to save history", ex);
            }
        });
    }

}
