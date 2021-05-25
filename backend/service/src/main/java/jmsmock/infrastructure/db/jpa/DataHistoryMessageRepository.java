package jmsmock.infrastructure.db.jpa;

import jmsmock.domain.model.HistoryMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DataHistoryMessageRepository extends JpaRepository<HistoryMessage, UUID> {

    List<HistoryMessage> findAllByReferenceKey(String referenceKey);

}
