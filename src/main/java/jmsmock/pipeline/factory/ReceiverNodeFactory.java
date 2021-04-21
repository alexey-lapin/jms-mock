package jmsmock.pipeline.factory;

import jmsmock.domain.NodeConfig;
import jmsmock.domain.ReceiverConfig;
import jmsmock.pipeline.impl.ListenerTrigger;
import jmsmock.pipeline.Node;
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
public class ReceiverNodeFactory implements NodeFactory {

    public static final String PARAMETER_RECEIVER_NAME = "receiver-name";

    private final ReceiverConfigService receiverConfigService;

    private final JmsListenerEndpointRegistry jmsListenerEndpointRegistry;
    private final MessageConverter messageConverter;
    private final JmsListenerContainerFactory<?> jmsListenerContainerFactory;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String receiverName = nodeConfig.getParameter(PARAMETER_RECEIVER_NAME)
                .orElseThrow(() -> new RuntimeException(PARAMETER_RECEIVER_NAME + " is empty"));

        ReceiverConfig receiverConfig = receiverConfigService.findByName(receiverName)
                .orElseThrow(() -> new RuntimeException(receiverName + " does not exist"));

        String destination = receiverConfig.getDestination();

        ListenerTrigger listener = new ListenerTrigger(messageConverter);
        listener.setNodeConfig(nodeConfig);

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
