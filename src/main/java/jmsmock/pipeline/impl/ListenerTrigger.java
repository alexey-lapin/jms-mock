package jmsmock.pipeline.impl;

import jmsmock.pipeline.AbstractNode;
import jmsmock.pipeline.Context;
import jmsmock.pipeline.Trigger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jms.support.converter.MessageConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.jms.Message;
import javax.jms.MessageListener;

@RequiredArgsConstructor
public class ListenerTrigger extends AbstractNode implements Trigger, MessageListener {

    private final Sinks.Many<Context> sink = Sinks.many().unicast().onBackpressureBuffer();

    private final MessageConverter messageConverter;

    @Override
    public Flux<Context> getFlux() {
        return sink.asFlux();
    }

    @Override
    public void onMessage(Message message) {
        Context context = new Context();
        org.springframework.messaging.Message<String> inboundMessage = convertMessage(message);
        context.setAttribute(Context.INBOUND_MESSAGE, inboundMessage);
        sink.emitNext(context, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private org.springframework.messaging.Message<String> convertMessage(Message message) {
        return (org.springframework.messaging.Message<String>) messageConverter.fromMessage(message);
    }
    
}
