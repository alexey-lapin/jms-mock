package jmsmock.service.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.UUID;

public class RandomValueHelper implements Helper<Object> {

    @Override
    public Object apply(Object input, Options options) throws IOException {
        String type = options.hash("type", "uuid");
        return UUID.randomUUID();
    }

}
