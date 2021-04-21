package jmsmock.pipeline.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@RequiredArgsConstructor
@Component
public class Test implements InitializingBean {

    @Resource(name = "of")
    private final ObjectProvider<String> of;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }

}

@Configuration
class Conf {

    @Bean
    ObjectProvider<String> of() {
        return new ObjectProvider<String>() {
            @Override
            public String getObject(Object... args) throws BeansException {
                return null;
            }

            @Override
            public String getIfAvailable() throws BeansException {
                return null;
            }

            @Override
            public String getIfUnique() throws BeansException {
                return null;
            }

            @Override
            public String getObject() throws BeansException {
                return null;
            }
        };
    }

}
