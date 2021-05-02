package jmsmock.application.pipeline;

import jmsmock.domain.model.NodeConfig;

public interface Node {

    NodeConfig getNodeConfig();

    Node getPrevious();

    void setPrevious(Node node);

    Node getNext();

    void setNext(Node node);

}
