package jmsmock.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
import javax.persistence.OrderBy;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MockConfig {

    @Id
    private UUID id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("position ASC")
    @JoinColumn(name = "mock_id")
    private SortedSet<NodeConfig> nodes;

    public void setNodes(@NonNull Set<NodeConfig> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

}
