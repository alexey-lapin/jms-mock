package jmsmock.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TriggearbleSignalDto {

    private List<ParameterDto> parameters;

}
