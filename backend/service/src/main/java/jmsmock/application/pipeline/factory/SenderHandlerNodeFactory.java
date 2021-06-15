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
package jmsmock.application.pipeline.factory;

import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.ContextConfigurer;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.SenderHandlerNode;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.SenderConfig;
import jmsmock.infrastructure.endpoint.SenderOperations;
import jmsmock.service.EventService;
import jmsmock.service.config.SenderConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SenderHandlerNodeFactory implements NodeFactory {

    private final EventService eventService;

    private final SenderConfigService senderConfigService;

    private final SenderOperations jmsSenderOperations;
    private final SenderOperations rabbitSenderOperations;

    @Override
    public Node create(MockConfig mockConfig, NodeConfig nodeConfig) {
        String senderName = nodeConfig.getParameter(SenderHandlerNode.PARAMETER_SENDER_NAME)
                .orElseThrow(() -> new RuntimeException(SenderHandlerNode.PARAMETER_SENDER_NAME + " is required"));

        SenderConfig senderConfig = senderConfigService.findByName(senderName)
                .orElseThrow(() -> new RuntimeException(senderName + " does not exist"));

        String type = senderConfig.getParameter("type").orElse("");
        switch (type) {
            case "jms":
            default:
                return jms(nodeConfig, senderConfig);
            case "rabbit":
                return rabbit(nodeConfig, senderConfig);
        }

    }

    private Node jms(NodeConfig nodeConfig, SenderConfig senderConfig) {
        String destination = senderConfig.getDestination();
        ContextConfigurer contextConfigurer = context -> {
            context.setAttributeIfAbsent(Context.DESTINATION, destination);
        };
        return new SenderHandlerNode(nodeConfig, eventService, jmsSenderOperations, contextConfigurer);
    }

    private Node rabbit(NodeConfig nodeConfig, SenderConfig senderConfig) {
        Optional<String> exchange = senderConfig.getParameter("exchange");
        Optional<String> routingKey = senderConfig.getParameter("routingKey");
        ContextConfigurer contextConfigurer = context -> {
            exchange.ifPresent(value -> context.setAttributeIfAbsent(Context.EXCHANGE, value));
            routingKey.ifPresent(value -> context.setAttributeIfAbsent(Context.ROUTING_KEY, value));
        };
        return new SenderHandlerNode(nodeConfig, eventService, rabbitSenderOperations, contextConfigurer);
    }

}
