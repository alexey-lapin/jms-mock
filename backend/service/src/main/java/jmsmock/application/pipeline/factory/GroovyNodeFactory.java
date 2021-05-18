package jmsmock.application.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.application.pipeline.Node;
import jmsmock.application.pipeline.impl.GroovyNode;
import jmsmock.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GroovyNodeFactory implements NodeFactory {

    private final ScriptEvaluator scriptEvaluator;

    private final EventService eventService;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String script = nodeConfig.getParameter(GroovyNode.PARAMETER_SCRIPT)
                .orElseThrow(() -> new RuntimeException(GroovyNode.PARAMETER_SCRIPT + "is required"));

        return new GroovyNode(nodeConfig, eventService, scriptEvaluator, new StaticScriptSource(script));
    }

}
