package jmsmock.pipeline.impl;

import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class LoggingHandlerNode extends AbstractNode implements Handler {

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            log.info(context.toString());
            return context;
        });
    }

}
