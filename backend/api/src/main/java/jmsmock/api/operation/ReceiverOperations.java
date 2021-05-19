/*
 * MIT License
 *
 * Copyright (c) 2021 - present Alexey Lapin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
