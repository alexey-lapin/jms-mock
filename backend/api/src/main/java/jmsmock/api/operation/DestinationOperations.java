package jmsmock.api.operation;

import jmsmock.api.dto.BrowseDto;
import jmsmock.api.dto.DestinationDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DestinationOperations {

    @GetMapping("/queues")
    List<DestinationDto> findAll();

    @DeleteMapping("/queues/{name}")
    void delete(String name);

    @GetMapping("/queues/{name}/browse")
    List<BrowseDto> browse(@PathVariable String name);

    @GetMapping("/queues/{name}/count")
    int count(@PathVariable String name);

}
