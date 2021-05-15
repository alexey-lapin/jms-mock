import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import jmsmock.service.handlebars.JsonPathHelper;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Test2 {

    @Test
    void test1() throws Exception {
        Path path = Paths.get(getClass().getClassLoader()
                .getResource("t1.json").toURI());

        String input = Files.lines(path).collect(Collectors.joining("\n"));
        System.out.println(input);

        Path path2 = Paths.get(getClass().getClassLoader()
                .getResource("input1.json").toURI());

        String input2 = Files.lines(path2).collect(Collectors.joining("\n"));

        Map<Object, Object> map = new HashMap<>();
        map.put("inbound", MessageBuilder.withPayload(input2).build());

        Handlebars handlebars = new Handlebars();
        handlebars.with(EscapingStrategy.NOOP);
        handlebars.registerHelper("jsonPath", new JsonPathHelper());
        Template template = handlebars.compileInline(input);
        String output = template.apply(map);

        System.out.println(output);
    }

}
