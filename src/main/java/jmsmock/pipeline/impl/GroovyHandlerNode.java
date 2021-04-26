package jmsmock.pipeline.impl;

import jmsmock.domain.model.Event;
import jmsmock.domain.model.NodeConfig;
import jmsmock.mock.Mock;
import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import jmsmock.pipeline.Trigger;
import jmsmock.pipeline.Triggerable;
import jmsmock.service.EventService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GroovyHandlerNode extends AbstractNode implements Handler, Trigger, Triggerable {

    public static final String PARAMETER_SCRIPT = "script";

    private final Sinks.Many<Context> sink = Sinks.many().unicast().onBackpressureBuffer();

    private final EventService eventService;

    private final ScriptEvaluator scriptEvaluator;

    private final ScriptSource scriptSource;

    @Setter
    private Mock mock;

    public GroovyHandlerNode(NodeConfig nodeConfig,
                             EventService eventService,
                             ScriptEvaluator scriptEvaluator,
                             ScriptSource scriptSource) {
        super(nodeConfig);
        this.eventService = eventService;
        this.scriptEvaluator = scriptEvaluator;
        this.scriptSource = scriptSource;
    }

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
        String event = String.format("mock [name=%s] triggered", mock.getMockConfig().getName());
        log.info(event);
        eventService.emit(Event.info(event));
        context.setAttribute(Context.MOCK, mock);
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
