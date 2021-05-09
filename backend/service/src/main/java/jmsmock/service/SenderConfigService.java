package jmsmock.service;

import jmsmock.application.mock.Mock;
import jmsmock.domain.model.MockConfig;
import jmsmock.domain.model.SenderConfig;
import jmsmock.domain.model.ParametrizedConfig;
import jmsmock.domain.repository.SenderConfigRepository;
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
public class SenderConfigService {

    private final SenderConfigRepository repository;

    @Lazy
    private final MockManager mockManager;

    @Transactional(readOnly = true)
    public List<SenderConfig> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SenderConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public SenderConfig createSender(SenderConfig config) {
        repository.findByName(config.getName()).ifPresent(item -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("sender [name=%s] already exist", config.getName()));
        });

        config.getParameter(ParametrizedConfig.PARAM_DESTINATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "destination must be set"));

        return repository.save(config);
    }

    @Transactional
    public SenderConfig updateSender(String name, SenderConfig config) {
        SenderConfig existingSenderConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("sender [name=%s] does not exist", name)));

        existingSenderConfig.setName(config.getName());
        existingSenderConfig.setParameters(config.getParameters());

        findUsages(mockManager.getMocks().values(), name).forEach(mock -> {
            mockManager.unregisterMock(mock.getMockConfig());
            mockManager.registerMock(mock.getMockConfig());
        });

        return repository.save(existingSenderConfig);
    }

    @Transactional
    public void deleteSender(String name) {
        repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("sender [name=%s] does not exist", name)));

        String usages = findUsages(mockManager.getMocks().values(), name).stream()
                .map(Mock::getMockConfig)
                .map(MockConfig::getName)
                .collect(Collectors.joining(", "));
        if (!usages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("sender [name=%s] is used in [%s]", name, usages));
        }

        repository.deleteByName(name);
    }

    private Set<Mock> findUsages(Collection<Mock> mocks, String senderName) {
        Set<Mock> found = new HashSet<>();
        for (Mock mock : mocks) {
            mock.visitNodes(node -> {
                String usedSenderName = node.getNodeConfig()
                        .getParameter("sender-name")
                        .orElse(null);
                if (Objects.equals(senderName, usedSenderName)) {
                    found.add(mock);
                }
            });
        }
        return found;
    }

}
