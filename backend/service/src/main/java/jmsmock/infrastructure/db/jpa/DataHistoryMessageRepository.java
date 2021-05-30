package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.MessageHistoryItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DataHistoryMessageRepository extends JpaRepository<MessageHistoryItem, UUID> {

    List<MessageHistoryItem> findAllByReferenceKey(String referenceKey, Sort sort);

    void deleteAllByReferenceKey(String referenceKey);

}
