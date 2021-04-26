package jmsmock.mock;

import jmsmock.domain.model.MockConfig;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.NodeVisitor;
import jmsmock.pipeline.Trigger;
import lombok.Getter;
import org.springframework.util.Assert;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;


public class Mock {

    private final Node head;

    @Getter
    private final MockConfig mockConfig;

    private Disposable subscription;

    public Mock(Node head, MockConfig mockConfig) {
        Assert.isInstanceOf(Trigger.class, head, "first node must be an instance of Trigger.class");
        Assert.notNull(mockConfig, "mockConfig must not be null");
        this.head = head;
        this.mockConfig = mockConfig;
    }

    public Node getTrigger() {
        return head;
    }

    public void init() {
        Trigger trigger = (Trigger) this.head;
        trigger.setMock(this);
        AtomicReference<Flux<Context>> fluxRef = new AtomicReference<>(trigger.getFlux());

        visitNodes(node -> {
            if (node instanceof Handler) {
                Handler handler = (Handler) node;
                fluxRef.set(handler.handle(fluxRef.get()));
            }
        });

        subscription = fluxRef.get().subscribe();
    }

    public void stop() {
        subscription.dispose();
    }

    public void visitNodes(NodeVisitor visitor) {
        for (Node node = head; node != null; node = node.getNext()) {
            visitor.visit(node);
        }
    }

}
