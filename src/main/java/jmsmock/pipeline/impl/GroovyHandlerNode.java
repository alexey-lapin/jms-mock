package jmsmock.pipeline.impl;

import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import jmsmock.pipeline.Trigger;
import jmsmock.pipeline.Triggerable;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GroovyHandlerNode extends AbstractNode implements Handler, Trigger, Triggerable {

    private final Sinks.Many<Context> sink = Sinks.many().unicast().onBackpressureBuffer();

    private final ScriptEvaluator scriptEvaluator;

    private final ScriptSource scriptSource;

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            Map<String, Object> args = new HashMap<>();
            args.put("context", context);
            scriptEvaluator.evaluate(scriptSource, args);
            return context;
        });
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
