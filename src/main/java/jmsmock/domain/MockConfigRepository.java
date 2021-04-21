package jmsmock.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MockConfigRepository extends JpaRepository<MockConfig, UUID> {

    Optional<MockConfig> findByName(String name);

    void deleteByName(String name);

}
