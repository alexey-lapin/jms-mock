package jmsmock.domain.repository;

import jmsmock.domain.model.MessageHistoryItem;

import java.util.List;

public interface MessageHistoryItemRepository {

    MessageHistoryItem save(MessageHistoryItem message);

    List<MessageHistoryItem> findAllByReferenceKey(String referenceKey);

    void deleteAllByReferenceKey(String referenceKey);

}
