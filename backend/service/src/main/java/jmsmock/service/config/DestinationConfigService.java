package jmsmock.service.config;

import jmsmock.domain.model.DestinationConfig;
import jmsmock.domain.repository.DestinationConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DestinationConfigService {

    private final DestinationConfigRepository repository;

    @Transactional
    public List<DestinationConfig> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<DestinationConfig> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public DestinationConfig createDestination(DestinationConfig config) {
        repository.findByName(config.getName()).ifPresent(item -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("destination [name=%s] already exist", config.getName()));
        });

        return repository.save(config);
    }

    @Transactional
    public DestinationConfig updateDestination(String name, DestinationConfig config) {
        DestinationConfig existingConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("destination [name=%s] does not exist", name)));

        existingConfig.setName(config.getName());
        // usages

        return repository.save(existingConfig);
    }

    @Transactional
    public void deleteDestination(String name) {
        repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("destination [name=%s] does not exist", name)));

        // usages
        repository.deleteByName(name);
    }

}
