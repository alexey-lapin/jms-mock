package jmsmock.pipeline;

import reactor.core.publisher.Flux;

public interface Trigger {

    Flux<Context> getFlux();

}
