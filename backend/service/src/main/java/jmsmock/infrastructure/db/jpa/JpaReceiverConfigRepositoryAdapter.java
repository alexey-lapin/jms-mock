package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.ReceiverConfig;
import jmsmock.domain.repository.ReceiverConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaReceiverConfigRepositoryAdapter implements ReceiverConfigRepository {

    private final DataReceiverConfigRepository repository;

    @Override
    public List<ReceiverConfig> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    @Override
    public Optional<ReceiverConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public ReceiverConfig save(ReceiverConfig config) {
        return repository.save(config);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

}
