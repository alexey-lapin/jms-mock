package jmsmock.application.pipeline;

import reactor.core.publisher.Flux;

public interface Handler {

    Flux<Context> handle(Flux<Context> stream);

}
