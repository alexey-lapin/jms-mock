package jmsmock.application.pipeline.impl;

import jmsmock.domain.model.Event;
import jmsmock.domain.model.NodeConfig;
import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
public class SenderHandlerNode extends AbstractNode implements Handler {

    public static final String PARAMETER_SENDER_NAME = "sender-name";

    private final EventService eventService;

    private final JmsTemplate jmsTemplate;

    private final String destination;

    public SenderHandlerNode(NodeConfig nodeConfig,
                             EventService eventService,
                             JmsTemplate jmsTemplate,
                             String destination) {
        super(nodeConfig);
        this.eventService = eventService;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            Optional<Message<String>> outboundMessage = context.getAttribute(Context.OUTBOUND_MESSAGE);
            if (outboundMessage.isPresent()) {
                try {
                    jmsTemplate.convertAndSend(destination, outboundMessage.get());
                } catch (Exception ex) {
                    log.error("failed to send message", ex);
                }
            } else {
                String event = String.format("mock [name=%s] sender [name=%s] skips message sending",
                        context.getAttribute(Context.MOCK).get().getMockConfig().getName(),
                        getNodeConfig().getParameter(PARAMETER_SENDER_NAME).get());
                eventService.emit(Event.warn(event));
                log.warn(event);
            }
            return context;
        });
    }

}
