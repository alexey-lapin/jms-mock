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
package jmsmock.service;

import jmsmock.application.pipeline.impl.ReceiverTriggerNode;
import jmsmock.infrastructure.endpoint.EndpointManager;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.NodeType;
import jmsmock.application.mock.Mock;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.Triggerable;
import jmsmock.application.pipeline.factory.NodeFactory;
import jmsmock.service.config.MockConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class MockManager {

    private final MockConfigService mockConfigService;
    private final NodeFactory nodeFactory;
    private final EventService eventService;
    private final EndpointManager endpointManager;

    private final Map<String, Mock> mocks = new ConcurrentHashMap<>();

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext().getBean(MockManager.class).initialize();
    }

    @Transactional(readOnly = true)
    public void initialize() {
        List<MockConfig> mocks = mockConfigService.findAll();

        for (MockConfig mockConfig : mocks) {
            if (mockConfig.isEnabled()) {
                registerMock(mockConfig);
            }
        }
    }

    public Map<String, Mock> getMocks() {
        return Collections.unmodifiableMap(mocks);
    }

    public void registerMock(MockConfig mockConfig) {
        Assert.notNull(mockConfig, "mockConfig must not be null");
        Assert.notEmpty(mockConfig.getNodes(), "mockConfig must have at least one node");

        Node head = null;
        Node previous = null;
        for (NodeConfig nodeConfig : mockConfig.getNodes()) {
            Node node = nodeFactory.create(mockConfig, nodeConfig);
            if (head == null) {
                head = node;
            }
            if (previous != null) {
                previous.setNext(node);
            }
            previous = node;
        }
        Mock mock = new Mock(head, mockConfig, eventService);
        mock.init();
        mocks.put(mockConfig.getName(), mock);
    }

    public void unregisterMock(MockConfig mockConfig) {
        Mock mock = mocks.remove(mockConfig.getName());
        if (mock != null) {
            mock.stop();

            NodeConfig triggerNodeConfig = mock.getTrigger().getNodeConfig();
            if (triggerNodeConfig.getType() == NodeType.RECEIVER) {
                endpointManager.unregister((ReceiverTriggerNode) mock.getTrigger());
            }
        }
    }

    public void toggleMock(String name) {
        Mock mock = mocks.get(name);
        if (mock != null) {
            if (mock.isRunning()) {
                mock.stop();
            } else {
                mock.init();
            }
        }
    }

    public void triggerMock(String name, Context context) {
        Mock mock = mocks.get(name);
        if (mock == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("mock [name=%s] does not exist", name));
        }
        Node trigger = mock.getTrigger();
        if (!(trigger instanceof Triggerable)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("mock [name=%s] is not triggerable", name));
        }
        Triggerable triggerable = (Triggerable) trigger;
        triggerable.trigger(context);
    }

}
