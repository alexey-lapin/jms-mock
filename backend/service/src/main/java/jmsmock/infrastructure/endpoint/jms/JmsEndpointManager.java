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
package jmsmock.infrastructure.endpoint.jms;

import jmsmock.application.pipeline.MessagingMessageListener;
import jmsmock.application.pipeline.impl.ReceiverTriggerNode;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.infrastructure.endpoint.EndpointManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;

@RequiredArgsConstructor
@Slf4j
//@Component
public class JmsEndpointManager implements EndpointManager {

    private final JmsListenerContainerFactory<?> listenerContainerFactory;
    private final JmsListenerEndpointRegistry listenerEndpointRegistry;
    private final MessageConverter messageConverter;

    @Override
    public void register(ReceiverTriggerNode receiver) {
        ReceiverConfig receiverConfig = receiver.getReceiverConfig();
        String receiverName = receiverConfig.getName();
        MessageListenerContainer listenerContainer = listenerEndpointRegistry.getListenerContainer(receiverName);
        if (listenerContainer == null) {
            JmsCompositeMessageListener compositeMessageListener = new JmsCompositeMessageListener();
            compositeMessageListener.addChild(wrap(receiver));

            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(receiverName);
            endpoint.setDestination(receiverConfig.getDestination());
            endpoint.setMessageListener(compositeMessageListener);

            boolean isEnabled = receiverConfig.isEnabled();
            listenerEndpointRegistry.registerListenerContainer(endpoint, listenerContainerFactory, isEnabled);
        } else {
            if (listenerContainer instanceof AbstractMessageListenerContainer) {
                Object messageListener = ((AbstractMessageListenerContainer) listenerContainer).getMessageListener();
                if (messageListener instanceof JmsCompositeMessageListener) {
                    ((JmsCompositeMessageListener) messageListener).addChild(wrap(receiver));
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
            if (messageListener instanceof JmsCompositeMessageListener) {
                ((JmsCompositeMessageListener) messageListener).removeChild(wrap(receiver));
            }
        }
    }

    @Override
    public void toggle(ReceiverConfig receiverConfig) {
        String receiverName = receiverConfig.getName();
        MessageListenerContainer listenerContainer = listenerEndpointRegistry.getListenerContainer(receiverName);
        if (listenerContainer == null) {
            log.warn("does not exist");
        } else {
            if (listenerContainer.isRunning()) {
                log.info("stopping");
                listenerContainer.stop();
            } else {
                log.info("starting");
                listenerContainer.start();
            }
        }
    }

    private JmsMessagingMessageListenerAdapter wrap(MessagingMessageListener listener) {
        return new JmsMessagingMessageListenerAdapter(listener, messageConverter);
    }

}
