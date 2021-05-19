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
package jmsmock.service;

import jmsmock.domain.model.MockConfig;
import jmsmock.domain.repository.MockConfigRepository;
import jmsmock.domain.model.NodeConfig;
import jmsmock.domain.model.NodeType;
import jmsmock.domain.model.ParametrizedConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MockConfigService {

    private final MockConfigRepository repository;

    private final ReceiverConfigService receiverConfigService;
    private final SenderConfigService senderConfigService;

    @Lazy
    private final MockManager mockManager;

    @Transactional(readOnly = true)
    public List<MockConfig> findAll() {
        return repository.findAll();
    }

    @Transactional
    public MockConfig createMock(MockConfig config) {
        repository.findByName(config.getName()).ifPresent(item -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("mock config [name=%s] already exists", config.getName()));
        });

        validateNodeConfigs(config);

        mockManager.registerMock(config);

        return repository.save(config);
    }

    @Transactional
    public MockConfig updateMock(String name, MockConfig config) {
        MockConfig existingMockConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("mock config [name=%s] does not exist", name)));

        validateNodeConfigs(config);

        mockManager.unregisterMock(existingMockConfig);

        existingMockConfig.setName(config.getName());
        existingMockConfig.setNodes(config.getNodes());

        mockManager.registerMock(existingMockConfig);

        return repository.save(existingMockConfig);
    }

    @Transactional
    public void deleteMock(String name) {
        MockConfig mockConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("mock config [name=%s] does not exist", name)));

        mockManager.unregisterMock(mockConfig);

        repository.deleteByName(name);
    }

    @Transactional
    public MockConfig toggleMock(String name) {
        MockConfig mockConfig = repository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("mock config [name=%s] does not exist", name)));

        mockConfig.setEnabled(!mockConfig.isEnabled());

        return repository.save(mockConfig);
    }

    private void validateNodeConfigs(MockConfig mockConfig) {
        NodeConfig headConfig = mockConfig.getNodes().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "mock must have at least one node"));

        if (!headConfig.getType().isTrigger()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "first node must be a trigger");
        }

        for (NodeConfig nodeConfig : mockConfig.getNodes()) {
            if (nodeConfig.getType() == NodeType.RECEIVER) {
                validateReceiverNodeConfig(nodeConfig);
            } else if (nodeConfig.getType() == NodeType.SENDER) {
                validateSenderNodeConfig(nodeConfig);
            }
        }
    }

    private void validateReceiverNodeConfig(NodeConfig nodeConfig) {
        String receiverName = nodeConfig.getParameter(ParametrizedConfig.PARAM_RECEIVER_NAME)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("node [type=%s] must have param [%s]",
                                nodeConfig.getType().getName(),
                                ParametrizedConfig.PARAM_RECEIVER_NAME)));
        receiverConfigService.findByName(receiverName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("receiver [name=%s] does not exist",
                                receiverName)));
    }

    private void validateSenderNodeConfig(NodeConfig nodeConfig) {
        String senderName = nodeConfig.getParameter(ParametrizedConfig.PARAM_SENDER_NAME)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("node [type=%s] must have param [%s]",
                                nodeConfig.getType().getName(),
                                ParametrizedConfig.PARAM_SENDER_NAME)));
        senderConfigService.findByName(senderName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("sender [name=%s] does not exist",
                                senderName)));
    }

}
