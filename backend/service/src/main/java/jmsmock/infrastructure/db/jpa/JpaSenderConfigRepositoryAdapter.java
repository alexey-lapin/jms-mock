package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.SenderConfig;
import jmsmock.domain.repository.SenderConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaSenderConfigRepositoryAdapter implements SenderConfigRepository {

    private final DataSenderConfigRepository repository;

    @Override
    public List<SenderConfig> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    @Override
    public Optional<SenderConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public SenderConfig save(SenderConfig config) {
        return repository.save(config);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

}
