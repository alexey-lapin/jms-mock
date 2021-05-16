package jmsmock.api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
public class ReceiverConfigDto {

    private UUID id;

    @NotBlank
    private String name;

    private Boolean isEnabled;

    @NotEmpty
    private List<ParameterDto> parameters;

    public Optional<Boolean> getIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

}
