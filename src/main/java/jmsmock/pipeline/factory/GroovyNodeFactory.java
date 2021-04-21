package jmsmock.pipeline.factory;

import jmsmock.domain.NodeConfig;
import jmsmock.pipeline.impl.GroovyHandlerNode;
import jmsmock.pipeline.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GroovyNodeFactory implements NodeFactory {

    private final ScriptEvaluator scriptEvaluator;

    @Override
    public Node create(NodeConfig nodeConfig) {
        String script = nodeConfig.getParameter("script")
                .orElseThrow(() -> new RuntimeException("no script"));
        GroovyHandlerNode node = new GroovyHandlerNode(scriptEvaluator, new StaticScriptSource(script));
        node.setNodeConfig(nodeConfig);
        return node;
    }

}
