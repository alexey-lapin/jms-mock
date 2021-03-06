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
package jmsmock.application.pipeline;

import jmsmock.application.mock.Mock;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {

    public static final AttributeKey<Mock> MOCK = AttributeKey.of("mock");
    public static final AttributeKey<Message<String>> INBOUND_MESSAGE = AttributeKey.of("inbound-message");
    public static final AttributeKey<Message<String>> OUTBOUND_MESSAGE = AttributeKey.of("outbound-message");
    public static final AttributeKey<String> DESTINATION = AttributeKey.of("destination");
    public static final AttributeKey<String> EXCHANGE = AttributeKey.of("exchange");
    public static final AttributeKey<String> ROUTING_KEY = AttributeKey.of("routing-key");

    private final Map<AttributeKey<?>, Object> map = new HashMap<>();

    public <T> Context setAttribute(AttributeKey<T> key, T value) {
        map.put(key, value);
        return this;
    }

    public <T> Context setAttributeIfAbsent(AttributeKey<T> key, T value) {
        map.putIfAbsent(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(AttributeKey<T> key) {
        return Optional.ofNullable((T) map.get(key));
    }

}
