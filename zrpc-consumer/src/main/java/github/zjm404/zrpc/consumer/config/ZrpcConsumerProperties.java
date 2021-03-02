package github.zjm404.zrpc.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zjm
 * @date 2021/2/25
 */
@Data
@ConfigurationProperties(prefix = "zrpc-consumer")
public class ZrpcConsumerProperties {
    private byte serializationCode = 2;
    private byte registryType = 1;
    private String registryAddr = "127.0.0.1:2181";
}
