package jmsmock.pipeline.impl;

import jmsmock.domain.model.NodeConfig;
import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DelayHandlerNode extends AbstractNode implements Handler {

    public static final String PARAM_PERIOD = "duration";

    private final long amount;

    public DelayHandlerNode(NodeConfig nodeConfig) {
        super(nodeConfig);
        amount = nodeConfig.getParameter(PARAM_PERIOD)
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException(PARAM_PERIOD + " is required"));
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.delayElements(Duration.of(amount, ChronoUnit.SECONDS));
    }

}
