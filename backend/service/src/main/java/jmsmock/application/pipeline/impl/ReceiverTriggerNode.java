package jmsmock.application.pipeline.impl;

import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Trigger;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
public class ReceiverTriggerNode extends AbstractNode implements Trigger, MessageListener {

    public static final String PARAMETER_RECEIVER_NAME = "receiver-name";

    private final Sinks.Many<Context> sink = Sinks.many().multicast().directBestEffort();

    private final EventService eventService;

    private final MessageConverter messageConverter;

    public ReceiverTriggerNode(NodeConfig nodeConfig, EventService eventService, MessageConverter messageConverter) {
        super(nodeConfig);
        this.eventService = eventService;
        this.messageConverter = messageConverter;
    }

    @Override
    public Flux<Context> getFlux() {
        return sink.asFlux();
    }

    @Override
    public void onMessage(Message message) {
        Context context = new Context();
        context.setAttribute(Context.INBOUND_MESSAGE, convertMessage(message));
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private org.springframework.messaging.Message<String> convertMessage(Message message) {
        return (org.springframework.messaging.Message<String>) messageConverter.fromMessage(message);
    }

}
