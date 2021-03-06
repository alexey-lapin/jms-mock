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

import jmsmock.infrastructure.endpoint.DestinationBrowser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.Message;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JmsDestinationBrowser implements DestinationBrowser {

    private final JmsOperations jmsOperations;
    private final MessageConverter messageConverter;

    @Override
    public List<Message<String>> browse(String name) {
        return jmsOperations.browse(name, (session, browser) -> convertMessages(browser));
    }

    @Override
    public int count(String name) {
        return jmsOperations.browse(name, (session, browser) -> getMessageList(browser).size());
    }

    @Override
    public void purge(String name) {
        javax.jms.Message message = null;
        do {
            message = jmsOperations.receive(name);
        } while (message != null);
    }

    @SuppressWarnings("unchecked")
    private List<javax.jms.Message> getMessageList(QueueBrowser browser) throws JMSException {
        return Collections.list((Enumeration<javax.jms.Message>) browser.getEnumeration());
    }

    @SneakyThrows
    private List<Message<String>> convertMessages(QueueBrowser browser) {
        return getMessageList(browser).stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Message<String> convert(javax.jms.Message jmsMessage) {
        Object object = messageConverter.fromMessage(jmsMessage);
        return (Message<String>) object;
    }

}
