package jmsmock.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
@Entity
public class MockConfig {

    @Id
    private UUID id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "mock_id")
    private Set<NodeConfig> nodes;

    public void setNodes(@NonNull Set<NodeConfig> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

}
