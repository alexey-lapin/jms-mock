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
package jmsmock.infrastructure.config;

import jmsmock.infrastructure.endpoint.DestinationBrowser;
import jmsmock.infrastructure.endpoint.EndpointManager;
import jmsmock.infrastructure.endpoint.noop.NoopDestinationBrowser;
import jmsmock.infrastructure.endpoint.noop.NoopEndpointManager;
import jmsmock.infrastructure.endpoint.noop.NoopSenderOperations;
import jmsmock.infrastructure.endpoint.SenderOperations;
import jmsmock.infrastructure.endpoint.jms.JmsDestinationBrowser;
import jmsmock.infrastructure.endpoint.jms.JmsEndpointManager;
import jmsmock.infrastructure.endpoint.jms.JmsSenderOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.jms.support.destination.JmsDestinationAccessor;

import javax.jms.ConnectionFactory;

public class JmsConfig {

    @ConditionalOnProperty(name = "app.jms.enabled", havingValue = "true")
    @Configuration
    static class Enabled {

        @Bean
        EndpointManager jmsEndpointManager(JmsListenerContainerFactory<?> jmsListenerContainerFactory,
                                           JmsListenerEndpointRegistry jmsListenerEndpointRegistry,
                                           MessageConverter messageConverter) {
            return new JmsEndpointManager(jmsListenerContainerFactory,
                    jmsListenerEndpointRegistry,
                    messageConverter);
        }

        @Bean
        SenderOperations jmsSenderOperations(JmsOperations jmsOperations) {
            return new JmsSenderOperations(jmsOperations);
        }

        @Bean
        DestinationBrowser jmsDestinationBrowser(JmsOperations jmsOperations, MessageConverter messageConverter) {
            return new JmsDestinationBrowser(jmsOperations, messageConverter);
        }

        @Bean
        MessageConverter jmsMessagingMessageConverter() {
            return new MessagingMessageConverter();
        }

        @Bean
        JmsOperations jmsTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter messagingMessageConverter) {
            JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
            jmsTemplate.setMessageConverter(messagingMessageConverter);
            jmsTemplate.setReceiveTimeout(JmsDestinationAccessor.RECEIVE_TIMEOUT_NO_WAIT);
            return jmsTemplate;
        }

    }

    @ConditionalOnProperty(name = "app.jms.enabled", havingValue = "false", matchIfMissing = true)
    @Configuration
    static class Disabled {

        @Bean
        EndpointManager jmsEndpointManager() {
            return new NoopEndpointManager();
        }

        @Bean
        SenderOperations jmsSenderOperations() {
            return new NoopSenderOperations();
        }

        @Bean
        DestinationBrowser jmsDestinationBrowser() {
            return new NoopDestinationBrowser();
        }

    }

}
