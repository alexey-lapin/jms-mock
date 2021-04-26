package jmsmock.pipeline;

import jmsmock.mock.Mock;
import reactor.core.publisher.Flux;

public interface Trigger {

    Flux<Context> getFlux();

    void setMock(Mock mock);

}
