package jmsmock.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.pipeline.Node;

public interface NodeFactory {

    Node create(NodeConfig nodeConfig);

}
