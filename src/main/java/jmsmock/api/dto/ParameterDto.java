package jmsmock.api.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ParameterDto {

    @NotBlank
    private String key;

    @NotNull
    private String value;

}
