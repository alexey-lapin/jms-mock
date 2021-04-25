package jmsmock.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.SenderConfig;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.impl.SenderHandlerNode;
import jmsmock.service.SenderConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderHandlerNodeFactory implements NodeFactory {

    private final SenderConfigService senderConfigService;

    private final JmsTemplate jmsTemplate;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String senderName = nodeConfig.getParameter(SenderHandlerNode.PARAMETER_SENDER_NAME)
                .orElseThrow(() -> new RuntimeException(SenderHandlerNode.PARAMETER_SENDER_NAME + " is required"));

        SenderConfig senderConfig = senderConfigService.findByName(senderName)
                .orElseThrow(() -> new RuntimeException(senderName + " does not exist"));

        String destination = senderConfig.getDestination();

        return new SenderHandlerNode(nodeConfig, jmsTemplate, destination);
    }

}
