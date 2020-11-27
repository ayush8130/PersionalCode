package com.kayra.market.kmorms.cms.integration.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * A central place to register application converters and formatters.
 */
@Configurable
public class ApplicationConversionServiceFactory extends FormattingConversionServiceFactoryBean {

    public static final String TIME_PATTERN = "HH:mm";

    public void installLabelConverters(FormatterRegistry registry) {

    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

    @Override
    public void setFormatterRegistrars(Set<FormatterRegistrar> formatterRegistrars) {
        super.setFormatterRegistrars(formatterRegistrars);
    }

}
