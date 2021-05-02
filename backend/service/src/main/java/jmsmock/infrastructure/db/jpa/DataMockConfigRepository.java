package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.MockConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataMockConfigRepository extends JpaRepository<MockConfig, UUID> {

    Optional<MockConfig> findByName(String name);

    void deleteByName(String name);

}