package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.ReceiverConfig;
import jmsmock.domain.repository.ReceiverConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaReceiverConfigRepositoryAdapter implements ReceiverConfigRepository {

    @Delegate
    private final DataReceiverConfigRepository repository;

    @Override
    public ReceiverConfig save(ReceiverConfig config) {
        return repository.save(config);
    }

}
