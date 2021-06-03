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
package jmsmock.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WebConfig implements WebMvcConfigurer {

    public static final String API_BASE_PATH_REPLACE = "<API_BASE_PATH_REPLACE>";
    public static final String PUBLIC_PATH_REPLACE = "<PUBLIC_PATH_REPLACE>";
    public static final String ROUTER_HISTORY_MODE_REPLACE = "<ROUTER_HISTORY_MODE_REPLACE>";
    public static final String ROUTER_HISTORY_MODE_HASH = "HASH";

    @Value("${ui.public-path:}")
    private String publicPath;

    @Value("${ui.api-base-path:${ui.public-path:}/api}")
    private String apiBasePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/public/index.html")
                .resourceChain(true)
                .addTransformer((request, resource, transformerChain) -> {
                    String content = readResourceToString(resource);
                    content = content.replaceAll(PUBLIC_PATH_REPLACE, publicPath);
                    return new TransformedResource(resource, content.getBytes());
                });
        registry.addResourceHandler("/js/app.*.js")
                .addResourceLocations("classpath:/public/js/")
                .resourceChain(true)
                .addTransformer((request, resource, transformerChain) -> {
                    String content = readResourceToString(resource);
                    content = content.replaceAll(PUBLIC_PATH_REPLACE, publicPath);
                    content = content.replaceAll(API_BASE_PATH_REPLACE, apiBasePath);
                    content = content.replaceAll(ROUTER_HISTORY_MODE_REPLACE, ROUTER_HISTORY_MODE_HASH);
                    return new TransformedResource(resource, content.getBytes());
                });
    }

    private static String readResourceToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
