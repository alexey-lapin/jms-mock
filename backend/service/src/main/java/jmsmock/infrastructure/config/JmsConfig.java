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

import com.ibm.mq.spring.boot.MQAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessagingMessageConverter;
import org.springframework.jms.support.destination.JmsDestinationAccessor;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsConfig {

    @Bean
    MessageConverter messagingMessageConverter() {
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

    @ConditionalOnProperty(name = "app.jms", havingValue = "activemq", matchIfMissing = false)
    @Import(ActiveMqConfiguration.class)
    static class ActiveMqConfiguration {
    }

    @ConditionalOnProperty(name = "app.jms", havingValue = "artemis", matchIfMissing = false)
    @Import(ActiveMqConfiguration.class)
    static class ArtemisConfiguration {
    }

    @ConditionalOnProperty(name = "app.jms", havingValue = "ibm", matchIfMissing = false)
    @Import(MQAutoConfiguration.class)
    static class IbmMqConfiguration {
    }

}
