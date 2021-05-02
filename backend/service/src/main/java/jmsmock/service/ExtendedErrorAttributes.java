package jmsmock.service;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ExtendedErrorAttributes extends DefaultErrorAttributes {

    @Override
    protected String getMessage(WebRequest webRequest, Throwable error) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getReason();
        }
        return super.getMessage(webRequest, error);
    }

}
