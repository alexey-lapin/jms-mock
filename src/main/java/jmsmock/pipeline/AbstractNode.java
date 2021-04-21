package jmsmock.pipeline;

import jmsmock.domain.NodeConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractNode implements Node {

    private NodeConfig nodeConfig;

    private Node previous;

    private Node next;

}
