package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.HistoryMessage;
import jmsmock.domain.repository.HistoryMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpsHistoryMessageRepositoryAdapter implements HistoryMessageRepository {

    private final DataHistoryMessageRepository repository;

    @Override
    public HistoryMessage save(HistoryMessage message) {
        return repository.save(message);
    }

    @Override
    public List<HistoryMessage> findAllByReferenceKey(String referenceKey) {
        return repository.findAllByReferenceKey(referenceKey);
    }

}
