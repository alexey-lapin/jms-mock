package jmsmock.pipeline.impl;

import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SenderHandlerNode extends AbstractNode implements Handler {

    private final JmsTemplate jmsTemplate;

    private final String destination;

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            Optional<Message<String>> outboundMessage = context.getAttribute(Context.OUTBOUND_MESSAGE);
            if (outboundMessage.isPresent()) {
                jmsTemplate.convertAndSend(destination, outboundMessage.get());
            } else {
                log.warn("no outbound message");
            }
            return context;
        });
    }

}
