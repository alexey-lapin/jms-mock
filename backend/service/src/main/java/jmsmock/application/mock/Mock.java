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
package jmsmock.application.mock;

import jmsmock.domain.model.Event;
import jmsmock.domain.model.MockConfig;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.NodeVisitor;
import jmsmock.application.pipeline.Trigger;
import jmsmock.service.EventService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class Mock {

    private final EventService eventService;

    private final Node head;

    @Getter
    private final MockConfig mockConfig;

    private Disposable subscription;

    public Mock(Node head, MockConfig mockConfig, EventService eventService) {
        Assert.isInstanceOf(Trigger.class, head, "first node must be an instance of Trigger.class");
        Assert.notNull(mockConfig, "mockConfig must not be null");
        this.head = head;
        this.mockConfig = mockConfig;
        this.eventService = eventService;
    }

    public Node getTrigger() {
        return head;
    }

    public void init() {
        Trigger trigger = (Trigger) this.head;

        Flux<Context> flux = trigger.getFlux()
                .publishOn(Schedulers.boundedElastic());
        flux = addLogger(flux, "started");
        flux = addContextAttribute(flux);
        AtomicReference<Flux<Context>> fluxRef = new AtomicReference<>(flux);

        visitNodes(node -> {
            if (node instanceof Handler) {
                Handler handler = (Handler) node;
                fluxRef.set(handler.handle(fluxRef.get()));
            }
        });

        flux = fluxRef.get();
        flux = addLogger(flux, "finished");
        subscription = flux.subscribe();
    }

    public void stop() {
        subscription.dispose();
    }

    public boolean isRunning() {
        return !subscription.isDisposed();
    }

    public void visitNodes(NodeVisitor visitor) {
        for (Node node = head; node != null; node = node.getNext()) {
            visitor.visit(node);
        }
    }

    private Flux<Context> addContextAttribute(Flux<Context> flux) {
        return flux.map(context -> {
            context.setAttribute(Context.MOCK, this);
            return context;
        });
    }

    private Flux<Context> addLogger(Flux<Context> flux, String state) {
        return flux.map(context -> {
            String message = String.format("mock [name=%s] %s", this.getMockConfig().getName(), state);
            log.info(message);
            eventService.emit(Event.info(message));
            return context;
        });
    }

}
