package jmsmock.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NodeConfigRepository extends JpaRepository<NodeConfig, UUID> {

//    Optional<NodeConfig> findByName(String name);

//    void deleteByName(String name);

}
