package github.zjm404.zrpc.provider.config;

import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 自动配置类
 * @author zjm
 * @date 2021/1/26
 */
@Configuration
@ConditionalOnClass({ProviderContext.class})
@EnableConfigurationProperties(ZrpcProviderProperties.class)
@Slf4j
public class ZrpcProviderAutoConfiguration {
    @Resource
    private ZrpcProviderProperties properties;

    @Bean
    @ConditionalOnMissingBean(ProviderContext.class)
    public ProviderContext init() throws Exception {
        log.info("properties:{}",properties);
        RegistryService registryService = RegistryServiceFactory.getRegistryService(properties.getRegistryType(), properties.getRegistryAddr());
        return new ProviderContext(properties.getServicePort(),registryService);
    }
}
