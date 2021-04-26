package jmsmock.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DestinationBrowser {

    private final JmsTemplate jmsTemplate;

    public List<Message<String>> browse(String name) {
        return jmsTemplate.browse(name, (session, browser) -> convertMessages(browser));
    }

    public int count(String name) {
        return jmsTemplate.browse(name, (session, browser) -> getMessageList(browser).size());
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
        Object object = jmsTemplate.getMessageConverter().fromMessage(jmsMessage);
        return (Message<String>) object;
    }

}
