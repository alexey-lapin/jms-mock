package jmsmock.application.pipeline.factory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.TemplateHandlerNode;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.Parameter;
import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TemplateHandlerNodeFactory implements NodeFactory {

    private static final ParserContext PARSER_CONTEXT = new TemplateParserContext();

    private final EventService eventService;

    private final ExpressionParser expressionParser;

    private final Handlebars handlebars;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String payloadTemplateInput = nodeConfig.getParameter(TemplateHandlerNode.PARAMETER_PAYLOAD_TEMPLATE)
                .orElseThrow(() -> new RuntimeException(TemplateHandlerNode.PARAMETER_PAYLOAD_TEMPLATE + " is required"));

        Template payloadTemplate = compilePayloadTemplate(payloadTemplateInput);

        Map<String, Expression> headerExpressions = nodeConfig.getParameters().stream()
                .filter(TemplateHandlerNodeFactory::isHeaderExpressionParam)
                .collect(Collectors.toMap(this::extractHeaderName, this::compileHeaderExpression));

        return new TemplateHandlerNode(nodeConfig, eventService, payloadTemplate, headerExpressions);
    }

    @SneakyThrows
    private Template compilePayloadTemplate(String payloadTemplateInput) {
        return handlebars.compileInline(payloadTemplateInput);
    }

    private Expression compileHeaderExpression(Parameter parameter) {
        return expressionParser.parseExpression(parameter.getValue(), PARSER_CONTEXT);
    }

    private static boolean isHeaderExpressionParam(Parameter parameter) {
        return parameter.getKey() != null
                && parameter.getKey().startsWith(TemplateHandlerNode.PARAMETER_HEADER_EXPRESSION_PREFIX);
    }

    private String extractHeaderName(Parameter parameter) {
        return parameter.getKey().replace(TemplateHandlerNode.PARAMETER_HEADER_EXPRESSION_PREFIX, "");
    }

}
