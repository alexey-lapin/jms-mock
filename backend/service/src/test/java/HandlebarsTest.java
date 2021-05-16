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

public class HandlebarsTest {

    @Test
    void test1() throws Exception {
        Path templatePath = Paths.get(getClass().getClassLoader()
                .getResource("template-1.json").toURI());

        String templateContent = Files.lines(templatePath).collect(Collectors.joining("\n"));
        System.out.println(templateContent);

        Path inputPath = Paths.get(getClass().getClassLoader()
                .getResource("input-1.json").toURI());

        String inputContent = Files.lines(inputPath).collect(Collectors.joining("\n"));

        Map<Object, Object> map = new HashMap<>();
        map.put("inbound", MessageBuilder.withPayload(inputContent).build());

        Handlebars handlebars = new Handlebars();
        handlebars.with(EscapingStrategy.NOOP);
        handlebars.registerHelper("jsonPath", new JsonPathHelper());
        Template template = handlebars.compileInline(templateContent);
        String output = template.apply(map);

        System.out.println(output);
    }

}
