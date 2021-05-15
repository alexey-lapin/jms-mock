import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;

public class Test3 {

    @Test
    void test1() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("'Any string'");
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test2() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("new String('pp')");
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test3() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("#{new String('pp')}", new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test4() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("qq #{new String('pp')} zz", new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test5() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("qq #{T(java.util.UUID).randomUUID()} zz", new TemplateParserContext());
        String result = (String) expression.getValue();

        System.out.println(result);
    }

    @Test
    void test6() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("#{['inbound'].headers['zz']}", new TemplateParserContext());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("inbound", MessageBuilder.withPayload("qwer").setHeader("zz", "qq").build());
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().withRootObject(map).build();
        String result = (String) expression.getValue(context);

        System.out.println(result);
    }

}
