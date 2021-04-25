package jmsmock.pipeline.impl;

import jmsmock.domain.model.NodeConfig;
import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
public class SenderHandlerNode extends AbstractNode implements Handler {

    public static final String PARAMETER_SENDER_NAME = "sender-name";

    private final JmsTemplate jmsTemplate;

    private final String destination;

    public SenderHandlerNode(NodeConfig nodeConfig, JmsTemplate jmsTemplate, String destination) {
        super(nodeConfig);
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            Optional<Message<String>> outboundMessage = context.getAttribute(Context.OUTBOUND_MESSAGE);
            if (outboundMessage.isPresent()) {
                log.info("sending");
                jmsTemplate.convertAndSend(destination, outboundMessage.get());
            } else {
                log.warn("no outbound message");
            }
            return context;
        });
    }

}
