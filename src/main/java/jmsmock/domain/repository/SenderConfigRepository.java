package jmsmock.domain.repository;

import jmsmock.domain.model.SenderConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SenderConfigRepository {

    List<SenderConfig> findAll();

    Optional<SenderConfig> findByName(String name);

    SenderConfig save(SenderConfig config);

    void deleteByName(String name);

}
