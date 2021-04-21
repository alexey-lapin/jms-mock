package jmsmock.converter;

import org.springframework.core.convert.converter.Converter;

public interface ModelConverter<S, T> extends Converter<S, T> {
}
