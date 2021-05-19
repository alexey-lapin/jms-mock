/*
 * MIT License
 *
 * Copyright (c) 2021 - present Alexey Lapin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmsmock.application.pipeline.impl;

import jmsmock.application.mock.Mock;
import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import jmsmock.application.pipeline.Trigger;
import jmsmock.application.pipeline.Triggerable;
import jmsmock.domain.model.Event;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GroovyNode extends AbstractNode implements Handler, Trigger, Triggerable {

    public static final String PARAMETER_SCRIPT = "script";

    private final Sinks.Many<Context> sink = Sinks.many().multicast().directBestEffort();

    private final EventService eventService;

    private final ScriptEvaluator scriptEvaluator;

    private final ScriptSource scriptSource;

    public GroovyNode(NodeConfig nodeConfig,
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
            try {
                Map<String, Object> args = new HashMap<>();
                args.put("context", context);
                scriptEvaluator.evaluate(scriptSource, args);
            } catch (Exception ex) {
                String mockName = context.getAttribute(Context.MOCK)
                        .map(Mock::getMockConfig)
                        .map(MockConfig::getName)
                        .orElse("unknown");
                String message = String.format("mock [name=%s] failed to execute groovy script: %s",
                        mockName,
                        ex.getMessage());
                eventService.emit(Event.error(message));
            }
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
