package github.zjm404.zrpc.provider.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为暴露的服务
 * @author zjm
 * @date 2021/1/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface ZrpcService {
    Class<?>  serviceInterface();
    String serviceVersion() default "1.0";
}
