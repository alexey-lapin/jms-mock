package jmsmock.application.pipeline.impl;

import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Trigger;
import jmsmock.application.pipeline.Triggerable;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class ComposerTriggerNode extends AbstractNode implements Trigger, Triggerable {

    private final Sinks.Many<Context> sink = Sinks.many().multicast().directBestEffort();

    private final EventService eventService;

    public ComposerTriggerNode(NodeConfig nodeConfig, EventService eventService) {
        super(nodeConfig);
        this.eventService = eventService;
    }

    @Override
    public Flux<Context> getFlux() {
        return sink.asFlux();
    }

    @Override
    public void trigger(Context context) {
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
