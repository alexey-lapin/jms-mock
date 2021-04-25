package jmsmock.pipeline;

import jmsmock.domain.model.NodeConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class AbstractNode implements Node {

    private final NodeConfig nodeConfig;

    private Node previous;

    private Node next;

}
