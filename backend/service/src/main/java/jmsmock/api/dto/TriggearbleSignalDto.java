package jmsmock.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
public class TriggearbleSignalDto {

    @NotNull
    private List<ParameterDto> parameters;

}
