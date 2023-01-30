package com.icefruit.courseteachingsystem.config;

import com.icefruit.courseteachingsystem.core.filter.AuthFilter;
import com.icefruit.courseteachingsystem.core.http.RequestDataExtractor;
import com.icefruit.courseteachingsystem.core.interceptor.AuthRequestInterceptor;
import com.icefruit.courseteachingsystem.core.interceptor.PreForwardRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({AppProperties.class})
@Import(value = CtsConfig.class)
public class GatewayConfiguration {

    protected final ServerProperties serverProperties;
    protected final AppProperties appProperties;

    public GatewayConfiguration(ServerProperties serverProperties,
                                AppProperties appProperties) {
        this.serverProperties = serverProperties;
        this.appProperties = appProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestDataExtractor faradayRequestDataExtractor() {
        return new RequestDataExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthFilter faradayReverseProxyFilter(
            RequestDataExtractor extractor,
            PreForwardRequestInterceptor requestInterceptor
    ) {
        return new AuthFilter(extractor, requestInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public PreForwardRequestInterceptor faradayPreForwardRequestInterceptor() {
        //return new NoOpPreForwardRequestInterceptor();
        return new AuthRequestInterceptor(appProperties.getSigningSecret());
    }

}
