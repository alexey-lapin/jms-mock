package jmsmock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class JmsListenerService {

    private final JmsListenerEndpointRegistry jmsListenerEndpointRegistry;

    public void toggle(String name) {
        MessageListenerContainer listenerContainer = jmsListenerEndpointRegistry.getListenerContainer(name);
        if (listenerContainer == null){
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

}
