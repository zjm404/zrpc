package github.zjm404.zrpc.consumer;

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
@Target(ElementType.TYPE)
@Component
public @interface ZrpcConsumer {
    String serviceVersion() default "1.0";
    String registryType() default "ZOOKEEPER";
    int timeout() default 5000;
    String registryAddr() default "";
}
