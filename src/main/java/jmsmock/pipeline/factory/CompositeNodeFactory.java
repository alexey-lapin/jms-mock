package jmsmock.pipeline.factory;

import jmsmock.domain.NodeConfig;
import jmsmock.domain.NodeType;
import jmsmock.pipeline.impl.LoggingHandlerNode;
import jmsmock.pipeline.impl.MessageCreatorTrigger;
import jmsmock.pipeline.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Primary
@Component
public class CompositeNodeFactory implements NodeFactory {

//    @Lazy
    private final NodeFactory receiverNodeFactory;
//    @Lazy
    private final NodeFactory senderNodeFactory;
//    @Lazy
    private final NodeFactory groovyNodeFactory;

    @Override
    public Node create(NodeConfig nodeConfig) {
        NodeType type = nodeConfig.getType();
        if (type == NodeType.RECEIVER) {
            return receiverNodeFactory.create(nodeConfig);
        } else if (type == NodeType.SENDER) {
            return senderNodeFactory.create(nodeConfig);
        } else if (type == NodeType.GROOVY) {
            return groovyNodeFactory.create(nodeConfig);
        } else if (type == NodeType.LOGGER) {
            LoggingHandlerNode node = new LoggingHandlerNode();
            node.setNodeConfig(nodeConfig);
            return node;
        } else if (type == NodeType.MESSAGE_CREATOR) {
            MessageCreatorTrigger node = new MessageCreatorTrigger();
            node.setNodeConfig(nodeConfig);
            return node;
        }

        throw new RuntimeException("node type is unknown");
    }

}
