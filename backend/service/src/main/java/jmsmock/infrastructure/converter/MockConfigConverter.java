package jmsmock.infrastructure.converter;

import jmsmock.api.dto.MockConfigDto;
import jmsmock.api.dto.NodeConfigDto;
import jmsmock.api.dto.ParameterDto;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class MockConfigConverter {

    @RequiredArgsConstructor
    @Component
    static class FromDto implements ModelConverter<MockConfigDto, MockConfig> {

        @Lazy
        private final ConversionService conversionService;

        @Override
        public MockConfig convert(MockConfigDto source) {
            List<NodeConfigDto> nodeConfigList = source.getNodes();
            SortedSet<NodeConfig> nodes = new TreeSet<>();
            for (int i = 0; i < nodeConfigList.size(); i++) {
                nodes.add(convertNodeConfig(nodeConfigList.get(i), i));
            }

            return new MockConfig(UUID.randomUUID(), source.getName(), nodes);
        }

        private NodeConfig convertNodeConfig(NodeConfigDto source, int position) {
            Set<Parameter> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, Parameter.class))
                    .collect(Collectors.toSet());

            NodeConfig nodeConfig = new NodeConfig();
            nodeConfig.setId(UUID.randomUUID());
            nodeConfig.setType(source.getType());
            nodeConfig.setPosition(position);
            nodeConfig.setParameters(parameters);
            return nodeConfig;
        }
    }

    @RequiredArgsConstructor
    @Component
    static class ToDto implements ModelConverter<MockConfig, MockConfigDto> {

        @Lazy
        private final ConversionService conversionService;

        @Override
        public MockConfigDto convert(MockConfig source) {
            List<NodeConfigDto> nodes = source.getNodes().stream()
                    .map(this::convertNodeConfig)
                    .collect(Collectors.toList());

            return MockConfigDto.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .nodes(nodes)
                    .build();
        }

        private NodeConfigDto convertNodeConfig(NodeConfig source) {
            List<ParameterDto> parameters = source.getParameters().stream()
                    .map(item -> conversionService.convert(item, ParameterDto.class))
                    .collect(Collectors.toList());

            return NodeConfigDto.builder()
                    .id(source.getId())
                    .type(source.getType())
                    .parameters(parameters)
                    .build();
        }
    }

}
