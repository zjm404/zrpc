package github.zjm404.zrpc.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zjm
 * @date 2021/1/26
 */
@Data
@ConfigurationProperties(prefix = "zrpc-provider")
public class ZrpcProviderProperties {
    private int servicePort = 2781;
    private String registryAddr = "127.0.0.1:2181";
    private byte registryType = 1;
}
