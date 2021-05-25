package jmsmock.domain.repository;

import jmsmock.domain.model.HistoryMessage;

import java.util.List;

public interface HistoryMessageRepository {

    HistoryMessage save(HistoryMessage message);

    List<HistoryMessage> findAllByReferenceKey(String referenceKey);

}
