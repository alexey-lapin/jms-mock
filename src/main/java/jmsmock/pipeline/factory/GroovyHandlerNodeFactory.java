package jmsmock.pipeline.factory;

import jmsmock.domain.model.NodeConfig;
import jmsmock.pipeline.Node;
import jmsmock.pipeline.impl.GroovyHandlerNode;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GroovyHandlerNodeFactory implements NodeFactory {

    private final ScriptEvaluator scriptEvaluator;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String script = nodeConfig.getParameter(GroovyHandlerNode.PARAMETER_SCRIPT)
                .orElseThrow(() -> new RuntimeException(GroovyHandlerNode.PARAMETER_SCRIPT + "is required"));

        return new GroovyHandlerNode(nodeConfig, scriptEvaluator, new StaticScriptSource(script));
    }

}
