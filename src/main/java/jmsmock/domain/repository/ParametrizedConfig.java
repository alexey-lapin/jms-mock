package jmsmock.domain.repository;

import jmsmock.domain.model.Parameter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface ParametrizedConfig {

    String PARAM_DESTINATION = "destination";
    String PARAM_SENDER_NAME = "sender-name";
    String PARAM_RECEIVER_NAME = "receiver-name";

    Set<Parameter> getParameters();

    void setParameters(Set<Parameter> parameters);

    default Map<String, String> getParameterMap() {
        return getParameters().stream().collect(Collectors.toMap(Parameter::getKey, Parameter::getValue));
    }

    default Optional<String> getParameter(String key) {
        return Optional.ofNullable(getParameterMap().get(key));
    }

}
