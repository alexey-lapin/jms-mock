/*
 * MIT License
 *
 * Copyright (c) 2021 - present Alexey Lapin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jmsmock.infrastructure.endpoint.rabbit;

import jmsmock.application.pipeline.MessagingMessageListener;
import jmsmock.application.pipeline.impl.ReceiverTriggerNode;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.infrastructure.endpoint.EndpointManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitEndpointManager implements EndpointManager {

    private final RabbitListenerContainerFactory<?> listenerContainerFactory;
    private final RabbitListenerEndpointRegistry listenerEndpointRegistry;
    private final MessageConverter messageConverter;

    @Override
    public void register(ReceiverTriggerNode receiver) {
        ReceiverConfig receiverConfig = receiver.getReceiverConfig();
        String receiverName = receiverConfig.getName();
        MessageListenerContainer listenerContainer = listenerEndpointRegistry.getListenerContainer(receiverName);
        if (listenerContainer == null) {
            RabbitCompositeMessageListener compositeMessageListener = new RabbitCompositeMessageListener();
            compositeMessageListener.addChild(wrap(receiver));

            SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
            endpoint.setId(receiverName);
            endpoint.setQueueNames(receiverConfig.getDestination());
            endpoint.setMessageListener(compositeMessageListener);

            boolean isEnabled = receiverConfig.isEnabled();
            listenerEndpointRegistry.registerListenerContainer(endpoint, listenerContainerFactory, isEnabled);
        } else {
            if (listenerContainer instanceof AbstractMessageListenerContainer) {
                Object messageListener = ((AbstractMessageListenerContainer) listenerContainer).getMessageListener();
                if (messageListener instanceof RabbitCompositeMessageListener) {
                    ((RabbitCompositeMessageListener) messageListener).addChild(wrap(receiver));
                }
            }
        }
    }

    @Override
    public void unregister(ReceiverTriggerNode receiver) {
        String receiverName = receiver.getReceiverConfig().getName();
        MessageListenerContainer listenerContainer = listenerEndpointRegistry.getListenerContainer(receiverName);
        if (listenerContainer instanceof AbstractMessageListenerContainer) {
            Object messageListener = ((AbstractMessageListenerContainer) listenerContainer).getMessageListener();
            if (messageListener instanceof RabbitCompositeMessageListener) {
                ((RabbitCompositeMessageListener) messageListener).removeChild(wrap(receiver));
            }
        }
    }

    private RabbitMessagingMessageListenerAdapter wrap(MessagingMessageListener listener) {
        return new RabbitMessagingMessageListenerAdapter(listener, messageConverter);
    }

}
