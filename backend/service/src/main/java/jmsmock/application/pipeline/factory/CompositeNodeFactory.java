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
    private final NodeFactory groovyHandlerNodeFactory;
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
                return groovyHandlerNodeFactory.create(nodeConfig);
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
