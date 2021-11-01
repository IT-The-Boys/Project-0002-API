package it.theboys.project0002api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {
    @Bean
    public ValidatingMongoEventListener mongoValidation(){
        return new ValidatingMongoEventListener(mongoValidator());
    }
    @Bean
    public LocalValidatorFactoryBean mongoValidator(){
        return new LocalValidatorFactoryBean();
    }
}
