package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.MockConfig;
import jmsmock.domain.repository.MockConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaMockConfigRepositoryAdapter implements MockConfigRepository {

    private final DataMockConfigRepository repository;

    @Override
    public List<MockConfig> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    @Override
    public Optional<MockConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public MockConfig save(MockConfig config) {
        return repository.save(config);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }

}
