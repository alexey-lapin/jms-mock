package jmsmock.pipeline;

import jmsmock.mock.Mock;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {

    public static final AttributeKey<Mock> MOCK = AttributeKey.of("mock");
    public static final AttributeKey<Message<String>> INBOUND_MESSAGE = AttributeKey.of("inbound-message");
    public static final AttributeKey<Message<String>> OUTBOUND_MESSAGE = AttributeKey.of("outbound-message");

    private final Map<AttributeKey<?>, Object> map = new HashMap<>();

    public <T> Context setAttribute(AttributeKey<T> key, T value) {
        map.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(AttributeKey<T> key) {
        return Optional.ofNullable((T) map.get(key));
    }

}
