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
