package jmsmock.infrastructure.converter;

import jmsmock.api.dto.ParameterDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import jmsmock.application.pipeline.Context;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContextConverter implements ModelConverter<TriggearbleSignalDto, Context> {

    @Override
    public Context convert(TriggearbleSignalDto source) {
        Map<String, String> parameters = source.getParameters().stream()
                .collect(Collectors.toMap(ParameterDto::getKey, ParameterDto::getValue));

        String payload = parameters.remove("payload");

        Context context = new Context();

        if (payload != null) {
            Message<String> message = MessageBuilder.withPayload(payload).copyHeaders(parameters).build();
            context.setAttribute(Context.OUTBOUND_MESSAGE, message);
        }

        return context;
    }

}
