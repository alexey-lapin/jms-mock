package jmsmock.domain.repository;

import jmsmock.domain.model.MockConfig;

import java.util.List;
import java.util.Optional;

public interface MockConfigRepository {

    List<MockConfig> findAll();

    Optional<MockConfig> findByName(String name);

    MockConfig save(MockConfig config);

    void deleteByName(String name);

}
