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
import jmsmock.application.pipeline.impl.ReceiverTriggerNode;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.infrastructure.endpoint.EndpointManager;
import jmsmock.service.EventService;
import jmsmock.service.config.ReceiverConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReceiverTriggerNodeFactory implements NodeFactory {

    private final EventService eventService;
    private final ReceiverConfigService receiverConfigService;
    private final EndpointManager endpointManager;

    @Override
    public Node create(MockConfig mockConfig, NodeConfig nodeConfig) {
        String receiverName = nodeConfig.getParameter(ReceiverTriggerNode.PARAMETER_RECEIVER_NAME)
                .orElseThrow(() -> new RuntimeException(ReceiverTriggerNode.PARAMETER_RECEIVER_NAME + " is required"));

        ReceiverConfig receiverConfig = receiverConfigService.findByName(receiverName)
                .orElseThrow(() -> new RuntimeException(receiverName + " does not exist"));

        ReceiverTriggerNode receiver = new ReceiverTriggerNode(nodeConfig, receiverConfig, eventService);

        endpointManager.register(receiver);

        return receiver;
    }

}
