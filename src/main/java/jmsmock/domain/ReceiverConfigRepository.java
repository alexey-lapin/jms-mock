package jmsmock.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReceiverConfigRepository extends JpaRepository<ReceiverConfig, UUID> {

    Optional<ReceiverConfig> findByName(String name);

    void deleteByName(String name);

}
