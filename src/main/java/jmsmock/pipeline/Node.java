package jmsmock.pipeline;

import jmsmock.domain.NodeConfig;

public interface Node {

    NodeConfig getNodeConfig();

    Node getPrevious();

    void setPrevious(Node node);

    Node getNext();

    void setNext(Node node);

}
