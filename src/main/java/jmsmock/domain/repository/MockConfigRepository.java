package jmsmock.domain.repository;

import jmsmock.domain.model.MockConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MockConfigRepository {

    List<MockConfig> findAll();

    Optional<MockConfig> findByName(String name);

    MockConfig save(MockConfig config);

    void deleteByName(String name);

}
