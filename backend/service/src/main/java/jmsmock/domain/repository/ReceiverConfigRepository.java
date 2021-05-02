package jmsmock.domain.repository;

import jmsmock.domain.model.ReceiverConfig;

import java.util.List;
import java.util.Optional;

public interface ReceiverConfigRepository {

    List<ReceiverConfig> findAll();

    Optional<ReceiverConfig> findByName(String name);

    ReceiverConfig save(ReceiverConfig config);

    void deleteByName(String name);

}
