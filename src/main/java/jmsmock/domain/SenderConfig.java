package jmsmock.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class SenderConfig {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "config_id")
    private Set<Parameter> parameters;

    public Map<String, String> getParameterMap() {
        return parameters.stream().collect(Collectors.toMap(Parameter::getKey, Parameter::getValue));
    }

    public Optional<String> getParameter(String key) {
        return Optional.of(getParameterMap().get(key));
    }

    public String getDestination() {
        return getParameter("destination")
                .orElseThrow(() -> new RuntimeException());
    }

}
