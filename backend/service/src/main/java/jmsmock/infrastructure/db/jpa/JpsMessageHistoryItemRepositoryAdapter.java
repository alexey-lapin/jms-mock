package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.MessageHistoryItem;
import jmsmock.domain.repository.MessageHistoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpsMessageHistoryItemRepositoryAdapter implements MessageHistoryItemRepository {

    private final DataHistoryMessageRepository repository;

    @Override
    public MessageHistoryItem save(MessageHistoryItem message) {
        return repository.save(message);
    }

    @Override
    public List<MessageHistoryItem> findAllByReferenceKey(String referenceKey) {
        return repository.findAllByReferenceKey(referenceKey);
    }

    @Transactional
    @Override
    public void deleteAllByReferenceKey(String referenceKey) {
        repository.deleteAllByReferenceKey(referenceKey);
    }

}
