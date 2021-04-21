package jmsmock.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SenderConfigRepository extends JpaRepository<SenderConfig, UUID> {

    Optional<SenderConfig> findByName(String name);

    void deleteByName(String name);

}
