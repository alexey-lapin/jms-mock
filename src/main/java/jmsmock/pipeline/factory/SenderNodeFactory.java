package jmsmock.pipeline.factory;

import jmsmock.domain.NodeConfig;
import jmsmock.domain.SenderConfig;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.impl.SenderHandlerNode;
import jmsmock.service.SenderConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SenderNodeFactory implements NodeFactory {

    public static final String PARAMETER_SENDER_NAME = "sender-name";

    private final SenderConfigService senderConfigService;

    private final JmsTemplate jmsTemplate;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String senderName = nodeConfig.getParameter(PARAMETER_SENDER_NAME)
                .orElseThrow(() -> new RuntimeException(PARAMETER_SENDER_NAME + " is empty"));

        SenderConfig senderConfig = senderConfigService.findByName(senderName)
                .orElseThrow(() -> new RuntimeException(senderName + " does not exist"));

        String destination = senderConfig.getDestination();

        SenderHandlerNode node = new SenderHandlerNode(jmsTemplate, destination);
        node.setNodeConfig(nodeConfig);
        return node;
    }

}
