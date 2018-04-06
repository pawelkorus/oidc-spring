package io.github.pawelkorus.soidc.springboot;

import io.github.pawelkorus.soidc.spring.DefaultOIDCUserDetailsService;
import io.github.pawelkorus.soidc.spring.OIDCUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OIDCAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OIDCUserDetailsService.class)
    public OIDCUserDetailsService defaultOIDCUserDetailsService() {
        return new DefaultOIDCUserDetailsService();
    }

}
