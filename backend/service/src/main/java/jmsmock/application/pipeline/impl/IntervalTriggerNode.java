package jmsmock.application.pipeline.impl;

import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Trigger;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class IntervalTriggerNode extends AbstractNode implements Trigger {

    public static final String PARAM_PERIOD = "duration";

    private final EventService eventService;

    private final long amount;

    public IntervalTriggerNode(NodeConfig nodeConfig, EventService eventService) {
        super(nodeConfig);
        this.eventService = eventService;
        amount = nodeConfig.getParameter(PARAM_PERIOD)
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException(PARAM_PERIOD + " is required"));
    }

    @Override
    public Flux<Context> getFlux() {
        return Flux.interval(Duration.ofSeconds(amount)).map(l -> new Context());
    }

}
