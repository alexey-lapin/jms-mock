package jmsmock.api.dto;

import jmsmock.domain.NodeType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class NodeConfigDto {

    private UUID id;

    @NotNull
    private NodeType type;

    @NotNull
    private List<ParameterDto> parameters;

}
