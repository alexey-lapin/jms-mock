package jmsmock.config;

import jmsmock.pipeline.AttributeKey;
import jmsmock.pipeline.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GroovyRunner {

    private final ScriptEvaluator scriptEvaluator;

    @PostMapping("/groovy")
    String evaluate(@RequestBody String script) {
        StaticScriptSource source = new StaticScriptSource(script);
        Map<String, Object> args = new HashMap<>();
        Context context = new Context();
        context.setAttribute(AttributeKey.of("ttt"), "ggg");
        args.put("context", context);
        return scriptEvaluator.evaluate(source, args).toString();
    }

}
