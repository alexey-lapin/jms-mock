package jmsmock.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.impl.ReceiverTriggerNode;
import jmsmock.service.ReceiverConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReceiverTriggerNodeFactory implements NodeFactory {

    private final ReceiverConfigService receiverConfigService;

    private final JmsListenerContainerFactory<?> jmsListenerContainerFactory;
    private final JmsListenerEndpointRegistry jmsListenerEndpointRegistry;
    private final MessageConverter messageConverter;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String receiverName = nodeConfig.getParameter(ReceiverTriggerNode.PARAMETER_RECEIVER_NAME)
                .orElseThrow(() -> new RuntimeException(ReceiverTriggerNode.PARAMETER_RECEIVER_NAME + " is required"));

        ReceiverConfig receiverConfig = receiverConfigService.findByName(receiverName)
                .orElseThrow(() -> new RuntimeException(receiverName + " does not exist"));

        String destination = receiverConfig.getDestination();

        ReceiverTriggerNode listener = new ReceiverTriggerNode(nodeConfig, messageConverter);

        MessageListenerContainer listenerContainer = jmsListenerEndpointRegistry.getListenerContainer(receiverConfig.getName());
        if (listenerContainer == null) {
            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(receiverConfig.getName());
            endpoint.setDestination(destination);
            endpoint.setMessageListener(listener);

            jmsListenerEndpointRegistry.registerListenerContainer(endpoint, jmsListenerContainerFactory, true);
        } else {
            listenerContainer.setupMessageListener(listener);
            listenerContainer.start();
        }

        return listener;
    }

}
