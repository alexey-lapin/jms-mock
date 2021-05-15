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
