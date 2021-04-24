package jmsmock.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ReceiverConfig implements ParametrizedConfig {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "config_id")
    private Set<Parameter> parameters;

    public void setParameters(@NonNull Set<Parameter> parameters) {
        this.parameters.clear();
        this.parameters.addAll(parameters);
    }

    public String getDestination() {
        return getParameter(PARAM_DESTINATION)
                .orElseThrow(() -> new RuntimeException());
    }

}
