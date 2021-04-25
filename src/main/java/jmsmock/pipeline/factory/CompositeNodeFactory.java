package jmsmock.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.NodeType;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.impl.ComposerTriggerNode;
import jmsmock.pipeline.impl.DelayHandlerNode;
import jmsmock.pipeline.impl.LoggingHandlerNode;
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

    @Override
    public Node create(NodeConfig nodeConfig) {
        NodeType type = nodeConfig.getType();
        switch (type) {
            case COMPOSER:
                return new ComposerTriggerNode(nodeConfig);
            case DELAY:
                return new DelayHandlerNode(nodeConfig);
            case GROOVY:
                return groovyHandlerNodeFactory.create(nodeConfig);
            case LOGGER:
                return new LoggingHandlerNode(nodeConfig);
            case RECEIVER:
                return receiverTriggerNodeFactory.create(nodeConfig);
            case SENDER:
                return senderHandlerNodeFactory.create(nodeConfig);
            default:
                throw new RuntimeException("node type is unknown");
        }
    }

}
