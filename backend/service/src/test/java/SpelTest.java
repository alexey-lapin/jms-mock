import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;

public class SpelTest {

    private ExpressionParser expressionParser;

    @BeforeEach
    void setUp() {
        expressionParser = new SpelExpressionParser();
    }

    @Test
    void test1() {
        String expressionString = "'Any string'";
        Expression expression = expressionParser.parseExpression(expressionString);
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test2() {
        String expressionString = "new String('pp')";
        Expression expression = expressionParser.parseExpression(expressionString);
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test3() {
        String expressionString = "#{new String('pp')}";
        Expression expression = expressionParser.parseExpression(expressionString, new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test4() {
        String expressionString = "qq #{new String('pp')} zz";
        Expression expression = expressionParser.parseExpression(expressionString, new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test5() {
        String expressionString = "qq #{T(java.util.UUID).randomUUID()} zz";
        Expression expression = expressionParser.parseExpression(expressionString, new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test6() {
        String expressionString = "#{['inbound'].headers['zz']}";
        Expression expression = expressionParser.parseExpression(expressionString, new TemplateParserContext());

        HashMap<Object, Object> map = new HashMap<>();
        map.put("inbound", MessageBuilder.withPayload("qwer")
                .setHeader("zz", "qq")
                .build());
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding()
                .withRootObject(map)
                .build();
        String result = (String) expression.getValue(context);

        System.out.println(result);
    }

}
