package jmsmock.application.pipeline.factory;

import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.ComposerTriggerNode;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.repository.MessageHistoryItemRepository;
import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ComposerTriggerNodeFactory implements NodeFactory {

    private final EventService eventService;

    private final MessageHistoryItemRepository messageHistoryItemRepository;

    @Override
    public Node create(MockConfig mockConfig, NodeConfig nodeConfig) {
        return new ComposerTriggerNode(mockConfig, nodeConfig, eventService, messageHistoryItemRepository);
    }

}
