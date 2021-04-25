package jmsmock.service;

import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.repository.ParametrizedConfig;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.domain.repository.ReceiverConfigRepository;
import jmsmock.mock.Mock;
import jmsmock.pipeline.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReceiverConfigService {

    private final ReceiverConfigRepository repository;

    @Lazy
    private final MockManager mockManager;

    @Transactional(readOnly = true)
    public List<ReceiverConfig> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ReceiverConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public ReceiverConfig createReceiver(ReceiverConfig config) {
        repository.findByName(config.getName()).ifPresent(item -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "receiver [name=] already exist");
        });

        config.getParameter(ParametrizedConfig.PARAM_DESTINATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "destination must be set"));

        return repository.save(config);
    }

    @Transactional
    public ReceiverConfig updateReceiver(String name, ReceiverConfig config) {
        ReceiverConfig existingReceiverConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver [name=] does not exist"));

        existingReceiverConfig.setName(config.getName());
        existingReceiverConfig.setParameters(config.getParameters());

        // recreate mock if needed

        return repository.save(existingReceiverConfig);
    }

    @Transactional
    public void deleteReceiver(String name) {
        repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver [name=] does not exist"));

        // check mock
        boolean isReceiverUser = mockManager.getMocks().values().stream()
                .anyMatch(mock -> hasReceiver(mock, name));

        if (isReceiverUser) {
            throw new RuntimeException("receiver is in use");
        }

        repository.deleteByName(name);
    }

    private boolean hasReceiver(Mock mock, String receiverName) {
        for (Node node = mock.getTrigger(); node != null; node = node.getNext()) {
            NodeConfig nodeConfig = node.getNodeConfig();
            String usedReceiverName = nodeConfig.getParameter("receiver-name").orElse(null);
            if (receiverName.equals(usedReceiverName)) {
                return true;
            }
        }
        return false;
    }

}
