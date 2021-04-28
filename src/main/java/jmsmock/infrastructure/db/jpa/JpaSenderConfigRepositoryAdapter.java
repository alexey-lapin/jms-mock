package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.SenderConfig;
import jmsmock.domain.repository.SenderConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaSenderConfigRepositoryAdapter implements SenderConfigRepository {

    @Delegate
    private final DataSenderConfigRepository repository;

    @Override
    public SenderConfig save(SenderConfig config) {
        return repository.save(config);
    }

}
