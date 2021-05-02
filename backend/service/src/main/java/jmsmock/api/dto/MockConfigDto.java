package jmsmock.api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class MockConfigDto {

    private UUID id;

    @NotBlank
    private String name;

    @NotEmpty
    private List<NodeConfigDto> nodes;

}
