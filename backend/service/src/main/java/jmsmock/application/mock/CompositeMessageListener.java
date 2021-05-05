package jmsmock.application.mock;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        children.forEach(child -> child.onMessage(message));
    }

}
