package jmsmock.pipeline.impl;

import jmsmock.domain.model.NodeConfig;
import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Trigger;
import jmsmock.pipeline.Triggerable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ComposerTriggerNode extends AbstractNode implements Trigger, Triggerable {

    private final Sinks.Many<Context> sink = Sinks.many().unicast().onBackpressureBuffer();

    public ComposerTriggerNode(NodeConfig nodeConfig) {
        super(nodeConfig);
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
