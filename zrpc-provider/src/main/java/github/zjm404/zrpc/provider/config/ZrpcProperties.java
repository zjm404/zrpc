package github.zjm404.zrpc.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zjm
 * @date 2021/1/26
 */
@Data
@ConfigurationProperties(prefix = "zrpc")
public class ZrpcProperties {
    private int servicePort;
    private String registryAddr;
    private String registryType;
}
