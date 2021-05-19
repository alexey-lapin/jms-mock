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
package jmsmock.service.handlebars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

import java.io.IOException;

public class JsonPathHelper implements Helper<Object> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object apply(Object input, Options options) throws IOException {
        if (input == null) {
            return "";
        }

        if (options == null || options.param(0, null) == null) {
            throw new RuntimeException("json path is empty");
        }

        String jsonPath = options.param(0);

        try {
            DocumentContext jsonDocument = getJsonDocument(input, options);
            Object result = getValue(jsonPath, jsonDocument, options);
            return objectMapper.writeValueAsString(result);
        } catch (InvalidJsonException e) {
            throw new RuntimeException("json is invalid");
        } catch (JsonPathException e) {
            throw new RuntimeException("json path is invalid");
        }
    }

    private Object getValue(String jsonPath, DocumentContext jsonDocument, Options options) {
        Object value;
//        RenderCache renderCache = getRenderCache(options);
//        RenderCache.Key cacheKey = RenderCache.Key.keyFor(Object.class, jsonPath, jsonDocument);
//        Object value = renderCache.get(cacheKey);
//        if (value == null) {
        value = jsonDocument.read(jsonPath);
//            renderCache.put(cacheKey, value);
//        }

        return value;
    }

    private DocumentContext getJsonDocument(Object json, Options options) {
        DocumentContext document;
//        RenderCache renderCache = getRenderCache(options);
//        RenderCache.Key cacheKey = RenderCache.Key.keyFor(DocumentContext.class, json);
//        DocumentContext document = renderCache.get(cacheKey);
//        if (document == null) {
        document = json instanceof String ?
                JsonPath.parse((String) json) :
                JsonPath.parse(json);
//            renderCache.put(cacheKey, document);
//        }

        return document;
    }
}
