package jmsmock.domain.repository;

import jmsmock.domain.model.SenderConfig;

import java.util.List;
import java.util.Optional;

public interface SenderConfigRepository {

    List<SenderConfig> findAll();

    Optional<SenderConfig> findByName(String name);

    SenderConfig save(SenderConfig config);

    void deleteByName(String name);

}
