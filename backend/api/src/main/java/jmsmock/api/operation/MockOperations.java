package jmsmock.api.operation;

import jmsmock.api.dto.MockConfigDto;
import jmsmock.api.dto.TriggearbleSignalDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

public interface MockOperations {

    @GetMapping("/mocks")
    List<MockConfigDto> getAllMocks();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/mocks")
    MockConfigDto createMock(@RequestBody MockConfigDto command);

    @PutMapping("/mocks/{name}")
    MockConfigDto updateMock(@PathVariable String name, @RequestBody MockConfigDto command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/mocks/{name}")
    void deleteMock(@PathVariable String name);

    @PostMapping("/mocks/{name}/toggle")
    MockConfigDto toggleMock(@PathVariable String name);

    @PostMapping("/mocks/{name}/trigger")
    void triggerMock(@PathVariable String name, @Valid @RequestBody TriggearbleSignalDto command);

}
