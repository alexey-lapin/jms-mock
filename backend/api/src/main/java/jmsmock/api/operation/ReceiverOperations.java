package jmsmock.api.operation;

import jmsmock.api.dto.ReceiverConfigDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface ReceiverOperations {

    @GetMapping("/receivers")
    List<ReceiverConfigDto> getAllReceivers();

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/receivers")
    ReceiverConfigDto createReceiver(@RequestBody ReceiverConfigDto command);

    @PutMapping("/receivers/{name}")
    ReceiverConfigDto updateReceiver(@PathVariable String name,
                                     @RequestBody ReceiverConfigDto command);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/receivers/{name}")
    void deleteReceiver(@PathVariable String name);

    @PostMapping("/receivers/{name}/toggle")
    ReceiverConfigDto toggleReceiver(@PathVariable String name);

}
