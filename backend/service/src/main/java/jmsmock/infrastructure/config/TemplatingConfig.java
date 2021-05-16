package jmsmock.infrastructure.config;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import jmsmock.service.handlebars.JsonPathHelper;
import jmsmock.service.handlebars.RandomValueHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class TemplatingConfig {

    @Bean
    ExpressionParser expressionParser() {
        return new SpelExpressionParser();
    }
    
    @Bean
    Handlebars handlebars() {
        Handlebars handlebars = new Handlebars();
        handlebars.with(EscapingStrategy.NOOP);
        handlebars.registerHelper("jsonPath", new JsonPathHelper());
        handlebars.registerHelper("random", new RandomValueHelper());
        return handlebars;
    }

}
