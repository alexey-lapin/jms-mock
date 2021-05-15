package jmsmock.infrastructure.config;

import com.github.jknack.handlebars.Handlebars;
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
        return new Handlebars();
    }

}
