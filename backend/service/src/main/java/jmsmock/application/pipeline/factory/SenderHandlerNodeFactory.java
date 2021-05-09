package jmsmock.application.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.SenderConfig;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.SenderHandlerNode;
import jmsmock.service.EventService;
import jmsmock.service.SenderConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderHandlerNodeFactory implements NodeFactory {

    private final EventService eventService;

    private final SenderConfigService senderConfigService;

    private final JmsTemplate jmsTemplate;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String senderName = nodeConfig.getParameter(SenderHandlerNode.PARAMETER_SENDER_NAME)
                .orElseThrow(() -> new RuntimeException(SenderHandlerNode.PARAMETER_SENDER_NAME + " is required"));

        SenderConfig senderConfig = senderConfigService.findByName(senderName)
                .orElseThrow(() -> new RuntimeException(senderName + " does not exist"));

        String destination = senderConfig.getDestination();

        return new SenderHandlerNode(nodeConfig, eventService, jmsTemplate, destination);
    }

}