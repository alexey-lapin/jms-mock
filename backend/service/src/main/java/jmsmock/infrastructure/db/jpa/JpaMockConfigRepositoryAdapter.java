package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.MockConfig;
import jmsmock.domain.repository.MockConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaMockConfigRepositoryAdapter implements MockConfigRepository {

    @Delegate
    private final DataMockConfigRepository repository;

    @Override
    public MockConfig save(MockConfig config) {
        return repository.save(config);
    }

}
