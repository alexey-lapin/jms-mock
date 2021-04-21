package jmsmock.infrastructure.web;

import jmsmock.pipeline.Context;
import jmsmock.pipeline.impl.MessageCreatorTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
//@RestController
public class RController {

    private final MessageCreatorTrigger messageCreator;

    @GetMapping("/trigger")
    void trigger() {
        messageCreator.trigger(new Context());
    }

}
