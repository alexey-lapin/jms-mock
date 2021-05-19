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

import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.ComposerTriggerNode;
import jmsmock.application.pipeline.impl.DelayHandlerNode;
import jmsmock.application.pipeline.impl.IntervalTriggerNode;
import jmsmock.application.pipeline.impl.LoggingHandlerNode;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.NodeType;
import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Primary
@Component
public class CompositeNodeFactory implements NodeFactory {

    private final NodeFactory receiverTriggerNodeFactory;
    private final NodeFactory senderHandlerNodeFactory;
    private final NodeFactory groovyNodeFactory;
    private final NodeFactory templateHandlerNodeFactory;

    private final EventService eventService;

    @Override
    public Node create(NodeConfig nodeConfig) {
        NodeType type = nodeConfig.getType();
        switch (type) {
            case COMPOSER:
                return new ComposerTriggerNode(nodeConfig, eventService);
            case DELAY:
                return new DelayHandlerNode(nodeConfig);
            case INTERVAL:
                return new IntervalTriggerNode(nodeConfig, eventService);
            case GROOVY:
                return groovyNodeFactory.create(nodeConfig);
            case LOGGER:
                return new LoggingHandlerNode(nodeConfig);
            case RECEIVER:
                return receiverTriggerNodeFactory.create(nodeConfig);
            case SENDER:
                return senderHandlerNodeFactory.create(nodeConfig);
            case TEMPLATE:
                return templateHandlerNodeFactory.create(nodeConfig);
            default:
                throw new RuntimeException("node type is unknown");
        }
    }

}
