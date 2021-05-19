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
