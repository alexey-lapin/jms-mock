package jmsmock.pipeline;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AttributeKey<T> {

    private final String name;

    public static <T> AttributeKey<T> of(String name) {
        return new AttributeKey<>(name);
    }

}
