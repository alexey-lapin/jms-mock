package jmsmock.application.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.application.pipeline.Node;

public interface NodeFactory {

    Node create(NodeConfig nodeConfig);

}
