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
package jmsmock.infrastructure;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigurationApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";
    private static final String PROPERTY_SOURCE_NAME = "priority-app";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        if (shouldDisableAutoConfiguration(environment)) {
            exclude(environment, getAutoConfigurationClass());
        }
    }

    private MapPropertySource getOrCreatePropertySource(ConfigurableEnvironment environment) {
        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            return (MapPropertySource) propertySources.get(PROPERTY_SOURCE_NAME);
        } else {
            MapPropertySource propertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, new HashMap<>());
            propertySources.addFirst(propertySource);
            return propertySource;
        }
    }

    protected void exclude(ConfigurableEnvironment environment, Class<?> aClass) {
        String excludedAutoConfigurations = environment.getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, "");
        if (!"".equals(excludedAutoConfigurations)) {
            excludedAutoConfigurations += ",";
        }
        excludedAutoConfigurations += aClass.getName();
        Map<String, Object> source = getOrCreatePropertySource(environment).getSource();
        source.put(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, excludedAutoConfigurations);
    }

    protected boolean shouldDisableAutoConfiguration(ConfigurableEnvironment environment) {
        Boolean isEnabled = environment.getProperty(getFlagPropertyName(), Boolean.class);
        if (isEnabled == null) {
            boolean isEnabledImplicitly = environment.containsProperty(getHintPropertyName());
            if (isEnabledImplicitly) {
                Map<String, Object> source = getOrCreatePropertySource(environment).getSource();
                source.put(getFlagPropertyName(), true);
            }
            return !isEnabledImplicitly;
        }
        return !isEnabled;
    }

    protected abstract Class<?> getAutoConfigurationClass();

    protected abstract String getFlagPropertyName();

    protected abstract String getHintPropertyName();

}
