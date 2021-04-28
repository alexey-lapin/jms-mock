package jmsmock.domain.repository;

import jmsmock.domain.model.ReceiverConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiverConfigRepository {

    List<ReceiverConfig> findAll();

    Optional<ReceiverConfig> findByName(String name);

    ReceiverConfig save(ReceiverConfig config);

    void deleteByName(String name);

}
