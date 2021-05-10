package jmsmock.application.mock;

import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class CompositeMessageListener implements MessageListener {

    private final List<MessageListener> children;

    public CompositeMessageListener() {
        this.children = new CopyOnWriteArrayList<>();
    }

    public void addChild(MessageListener child) {
        children.add(child);
    }

    public void removeChild(MessageListener child) {
        children.remove(child);
    }

    @Override
    public void onMessage(Message message) {
        if (children.isEmpty()) {
            log.warn("no children - discarding message");
        }
        children.forEach(child -> child.onMessage(message));
    }

}
