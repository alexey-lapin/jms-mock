package jmsmock.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class NodeConfig implements Comparable<NodeConfig>, ParametrizedConfig {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private NodeType type;

    private int position;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "config_id")
    private Set<Parameter> parameters;

    @Override
    public int compareTo(NodeConfig other) {
        return Integer.compare(position, other.position);
    }

}
