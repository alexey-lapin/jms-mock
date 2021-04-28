package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.ReceiverConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataReceiverConfigRepository extends JpaRepository<ReceiverConfig, UUID> {

    Optional<ReceiverConfig> findByName(String name);

    void deleteByName(String name);

}
