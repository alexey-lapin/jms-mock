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
