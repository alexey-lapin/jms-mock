package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.SenderConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataSenderConfigRepository extends JpaRepository<SenderConfig, UUID> {

    Optional<SenderConfig> findByName(String name);

    void deleteByName(String name);

}