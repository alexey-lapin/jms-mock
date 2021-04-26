package jmsmock.pipeline.impl;

import jmsmock.domain.model.Event;
import jmsmock.domain.model.NodeConfig;
import jmsmock.mock.Mock;
import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Trigger;
import jmsmock.pipeline.Triggerable;
import jmsmock.service.EventService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class ComposerTriggerNode extends AbstractNode implements Trigger, Triggerable {

    private final Sinks.Many<Context> sink = Sinks.many().unicast().onBackpressureBuffer();

    private final EventService eventService;

    @Setter
    private Mock mock;

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
        String event = String.format("mock [name=%s] triggered", mock.getMockConfig().getName());
        log.info(event);
        eventService.emit(Event.info(event));
        context.setAttribute(Context.MOCK, mock);
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
