package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.registry.RegistryType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zjm
 * @date 2021/1/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Component
public @interface ZrpcConsumer {
    String serviceVersion() default "1.0";
    // ZOOKEEPER code 为 1,默认为 zookeeper
    byte registryType() default 1;
    int timeout() default 5000;
    String registryAddr();
}
