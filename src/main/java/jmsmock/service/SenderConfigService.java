package jmsmock.service;

import jmsmock.api.dto.SenderConfigDto;
import jmsmock.domain.ReceiverConfig;
import jmsmock.domain.SenderConfig;
import jmsmock.domain.SenderConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SenderConfigService {

    private final SenderConfigRepository repository;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        });

        return repository.save(config);
    }

    @Transactional
    public SenderConfig updateSender(String name, SenderConfig config) {
        SenderConfig existingSenderConfig = repository.findByName(name).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        existingSenderConfig.setName(config.getName());
        existingSenderConfig.setParameters(config.getParameters());

        return repository.save(existingSenderConfig);
    }

    @Transactional
    public void deleteSender(String name) {
        repository.findByName(name).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        repository.deleteByName(name);
    }

}
