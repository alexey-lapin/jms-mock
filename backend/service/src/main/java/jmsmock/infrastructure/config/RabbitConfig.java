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

import jmsmock.infrastructure.endpoint.EndpointManager;
import jmsmock.infrastructure.endpoint.SenderOperations;
import jmsmock.infrastructure.endpoint.noop.NoopEndpointManager;
import jmsmock.infrastructure.endpoint.noop.NoopSenderOperations;
import jmsmock.infrastructure.endpoint.rabbit.RabbitEndpointManager;
import jmsmock.infrastructure.endpoint.rabbit.RabbitSenderOperations;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.MessagingMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class RabbitConfig {

    @ConditionalOnProperty(name = "app.rabbit.enabled", havingValue = "true")
    @Configuration
    static class Enabled {

        @Bean
        EndpointManager rabbitEndpointManager(RabbitListenerContainerFactory<?> rabbitListenerContainerFactory,
                                              RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry,
                                              MessageConverter messageConverter) {
            return new RabbitEndpointManager(rabbitListenerContainerFactory,
                    rabbitListenerEndpointRegistry,
                    messageConverter);
        }

        @Bean
        SenderOperations rabbitSenderOperations(RabbitOperations rabbitOperations) {
            return new RabbitSenderOperations(rabbitOperations);
        }

        @Bean
        MessageConverter rabbitMessagingMessageConverter() {
            return new MessagingMessageConverter();
        }

    }

    @ConditionalOnProperty(name = "app.rabbit.enabled", havingValue = "false", matchIfMissing = true)
    @Configuration
    static class Disabled {

        @Bean
        EndpointManager rabbitEndpointManager() {
            return new NoopEndpointManager();
        }

        @Bean
        SenderOperations rabbitSenderOperations() {
            return new NoopSenderOperations();
        }

    }

}
