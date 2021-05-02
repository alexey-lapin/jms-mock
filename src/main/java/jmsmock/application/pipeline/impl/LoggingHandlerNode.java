package jmsmock.application.pipeline.impl;

import jmsmock.domain.model.NodeConfig;
import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class LoggingHandlerNode extends AbstractNode implements Handler {

    public LoggingHandlerNode(NodeConfig nodeConfig) {
        super(nodeConfig);
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            log.info(context.toString());
            return context;
        });
    }

}
