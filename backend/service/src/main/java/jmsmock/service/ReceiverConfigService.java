package jmsmock.service;

import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.ReceiverConfig;
import jmsmock.domain.model.ParametrizedConfig;
import jmsmock.domain.repository.ReceiverConfigRepository;
import jmsmock.application.mock.Mock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReceiverConfigService {

    private final ReceiverConfigRepository repository;

    private final JmsListenerService jmsListenerService;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("receiver [name=%s] already exist", config.getName()));
        });

        config.getParameter(ParametrizedConfig.PARAM_DESTINATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "destination must be set"));

        return repository.save(config);
    }

    @Transactional
    public ReceiverConfig updateReceiver(String name, ReceiverConfig config) {
        ReceiverConfig existingReceiverConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("receiver [name=%s] does not exist", name)));

        existingReceiverConfig.setName(config.getName());
        existingReceiverConfig.setParameters(config.getParameters());

        // recreate mocks if needed
        findUsages(mockManager.getMocks().values(), name).forEach(mock -> {
            mockManager.unregisterMock(mock.getMockConfig());
            mockManager.registerMock(mock.getMockConfig());
        });

        return repository.save(existingReceiverConfig);
    }

    @Transactional
    public void deleteReceiver(String name) {
        repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("receiver [name=%s] does not exist", name)));

        String usages = findUsages(mockManager.getMocks().values(), name).stream()
                .map(Mock::getMockConfig)
                .map(MockConfig::getName)
                .collect(Collectors.joining(", "));
        if (!usages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("receiver [name=%s] is used in [%s]", name, usages));
        }

        repository.deleteByName(name);
    }

    public void toggle(String name) {
        repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("receiver [name=%s] does not exist", name)));

        jmsListenerService.toggle(name);
    }

    private Set<Mock> findUsages(Collection<Mock> mocks, String receiverName) {
        Set<Mock> found = new HashSet<>();
        for (Mock mock : mocks) {
            mock.visitNodes(node -> {
                String usedReceiverName = node.getNodeConfig()
                        .getParameter("receiver-name")
                        .orElse(null);
                if (Objects.equals(receiverName, usedReceiverName)) {
                    found.add(mock);
                }
            });
        }
        return found;
    }

}
