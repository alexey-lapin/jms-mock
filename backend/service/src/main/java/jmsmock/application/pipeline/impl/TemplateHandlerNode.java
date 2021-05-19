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
package jmsmock.application.pipeline.impl;

import com.github.jknack.handlebars.Template;
import jmsmock.application.pipeline.AbstractNode;
import jmsmock.application.pipeline.Context;
import jmsmock.application.pipeline.Handler;
import jmsmock.application.template.TemplateEvaluationContext;
import jmsmock.domain.model.Event;
import jmsmock.domain.model.NodeConfig;
import jmsmock.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class TemplateHandlerNode extends AbstractNode implements Handler {

    public static final String PARAMETER_PAYLOAD_TEMPLATE = "payload-template";
    public static final String PARAMETER_HEADER_EXPRESSION_PREFIX = "header-expression-";

    private final EventService eventService;
    private final Template payloadTemplate;
    private final Map<String, Expression> headerExpressions;

    public TemplateHandlerNode(NodeConfig nodeConfig,
                               EventService eventService,
                               Template payloadTemplate,
                               Map<String, Expression> headerExpressions) {
        super(nodeConfig);
        this.eventService = eventService;
        this.payloadTemplate = payloadTemplate;
        this.headerExpressions = headerExpressions;
    }

    @Override
    public Flux<Context> handle(Flux<Context> stream) {
        return stream.map(context -> {
            // could be other key - make configurable
            Message<String> inbound = context.getAttribute(Context.INBOUND_MESSAGE).orElse(null);
            try {
                TemplateEvaluationContext templateEvaluationContext = new TemplateEvaluationContext(inbound);
                StandardEvaluationContext evaluationContext = new StandardEvaluationContext(templateEvaluationContext);
                String payload = renderPayload(templateEvaluationContext);
                MessageHeaders headers = renderHeaders(evaluationContext);
                Message<String> outbound = MessageBuilder.createMessage(payload, headers);
                // could be other key - make configurable
                context.setAttribute(Context.OUTBOUND_MESSAGE, outbound);
            } catch (Exception ex) {
                log.error("failed to render template", ex);
                eventService.emit(Event.error(ex.getMessage()));
            }
            return context;
        });
    }

    private String renderPayload(TemplateEvaluationContext templateEvaluationContext) throws IOException {
        return payloadTemplate.apply(templateEvaluationContext);
    }

    private MessageHeaders renderHeaders(EvaluationContext evaluationContext) {
        MessageHeaderAccessor accessor = new MessageHeaderAccessor();
        for (Map.Entry<String, Expression> entry : headerExpressions.entrySet()) {
            accessor.setHeader(entry.getKey(), entry.getValue().getValue(evaluationContext));
        }
        return accessor.getMessageHeaders();
    }

}
