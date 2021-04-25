package jmsmock.service;

import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.NodeType;
import jmsmock.mock.Mock;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.Triggerable;
import jmsmock.pipeline.factory.NodeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class MockManager implements InitializingBean {

    private final MockConfigService mockConfigService;

    private final NodeFactory nodeFactory;

    private final JmsListenerEndpointRegistry jmsListenerEndpointRegistry;

    private final Map<String, Mock> mocks = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<MockConfig> mocks = mockConfigService.findAll();

        for (MockConfig mockConfig : mocks) {
            registerMock(mockConfig);
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
            Node node = nodeFactory.create(nodeConfig);
            if (head == null) {
                head = node;
            }
            if (previous != null) {
                previous.setNext(node);
            }
            previous = node;
        }
        Mock mock = new Mock(head, mockConfig);
        mock.init();
        mocks.put(mockConfig.getName(), mock);
    }

    public void unregisterMock(MockConfig mockConfig) {
        Mock mock = mocks.remove(mockConfig.getName());
        if (mock != null) {
            mock.stop();

            NodeConfig triggerNodeConfig = mock.getTrigger().getNodeConfig();
            if (triggerNodeConfig.getType() == NodeType.RECEIVER) {
                triggerNodeConfig.getParameter("receiver-name").ifPresent(receiverName -> {
                    MessageListenerContainer listenerContainer = jmsListenerEndpointRegistry.getListenerContainer(receiverName);
                    if (listenerContainer != null) {
                        listenerContainer.stop();
                    }
                });
            }
        }
    }

    public void triggerMock(String name, Context context) {
        Mock mock = mocks.get(name);
        if (mock == null) {
            throw new RuntimeException("no mock");
        }
        Node trigger = mock.getTrigger();
        if (!(trigger instanceof Triggerable)) {
            throw new RuntimeException("not triggerable");
        }
        Triggerable triggerable = (Triggerable) trigger;
        triggerable.trigger(context);
    }

}
