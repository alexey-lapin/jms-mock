package jmsmock.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;

@Configuration
public class ScriptingConfig {

    @Bean
    ScriptEvaluator groovyScriptEvaluator() {
        return new GroovyScriptEvaluator();
    }

}
