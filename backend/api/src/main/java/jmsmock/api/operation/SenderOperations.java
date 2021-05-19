package jmsmock.api.operation;

import jmsmock.api.dto.SenderConfigDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface SenderOperations {

    @GetMapping("/senders")
    List<SenderConfigDto> getAllSenders();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/senders")
    SenderConfigDto createSender(@RequestBody SenderConfigDto command);

    @PutMapping("/senders/{name}")
    SenderConfigDto updateSender(@PathVariable String name,
                                 @RequestBody SenderConfigDto command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/senders/{name}")
    void deleteSender(@PathVariable String name);

}
