package jmsmock.mock;

import jmsmock.domain.model.MockConfig;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.Trigger;
import org.springframework.util.Assert;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;


public class Mock {

    private final Node trigger;

    private final MockConfig mockConfig;

    private Disposable subscription;

    public Mock(Node trigger, MockConfig mockConfig) {
        Assert.isInstanceOf(Trigger.class, trigger, "trigger must be instance of Trigger.class");
        Assert.notNull(mockConfig, "mockConfig must not be null");
        this.trigger = trigger;
        this.mockConfig = mockConfig;
    }

    public Node getTrigger() {
        return trigger;
    }

    public void init() {
        Flux<Context> flux = ((Trigger) trigger).getFlux();

        for(Node node = trigger; node != null; node = node.getNext()) {
            if (node instanceof Handler) {
                Handler handler = (Handler) node;
                flux = handler.handle(flux);
            }
        }

        subscription = flux.subscribe();
    }

    public void stop() {
        subscription.dispose();
    }

}
