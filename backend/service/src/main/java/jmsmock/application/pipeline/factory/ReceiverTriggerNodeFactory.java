package jmsmock.application.pipeline.factory;

import jmsmock.application.mock.CompositeMessageListener;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.ReceiverTriggerNode;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.service.EventService;
import jmsmock.service.ReceiverConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReceiverTriggerNodeFactory implements NodeFactory {

    private final EventService eventService;

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

        ReceiverTriggerNode listener = new ReceiverTriggerNode(nodeConfig, eventService, messageConverter);

        MessageListenerContainer listenerContainer =
                jmsListenerEndpointRegistry.getListenerContainer(receiverConfig.getName());
        if (listenerContainer == null) {
            CompositeMessageListener compositeMessageListener = new CompositeMessageListener();
            compositeMessageListener.addChild(listener);
            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(receiverConfig.getName());
            endpoint.setDestination(destination);
            endpoint.setMessageListener(compositeMessageListener);

            jmsListenerEndpointRegistry.registerListenerContainer(endpoint,
                    jmsListenerContainerFactory,
                    receiverConfig.isEnabled());
        } else {
            if (listenerContainer instanceof AbstractMessageListenerContainer) {
                Object messageListener = ((AbstractMessageListenerContainer) listenerContainer).getMessageListener();
                if (messageListener instanceof CompositeMessageListener) {
                    ((CompositeMessageListener) messageListener).addChild(listener);
                }
            }
        }

        return listener;
    }

}
