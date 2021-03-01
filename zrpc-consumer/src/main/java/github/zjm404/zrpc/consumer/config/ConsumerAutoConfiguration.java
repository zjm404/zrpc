package github.zjm404.zrpc.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 消费服务自动配置类
 * @author zjm
 * @date 2021/2/26
 */
@Configuration
@ConditionalOnClass(ConsumerContext.class)
@EnableConfigurationProperties(ZrpcConsumerProperties.class)
@Slf4j
@AutoConfigureAfter(ZrpcConsumerProperties.class)
public class ConsumerAutoConfiguration {
    @Resource
    private ZrpcConsumerProperties properties;
    private static final String PREFIX = "zrpc-consumer";

    @Bean
    @ConditionalOnMissingBean(ConsumerContext.class)
    public static ConsumerContext initConsumer() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        //因为bean生命周期的原因，执行 initConsumer 时，properties的bean还没有生成，所以这里用从指定的文件的中获取内容好了，如果不存在则使用默认值
//        //config properties
//        Properties cp = PropertiesLoaderUtils.loadAllProperties("consumer.properties");
//        //default properties
//        ZrpcConsumerProperties dp =  new ZrpcConsumerProperties();
//        Field[] fields = ZrpcConsumerProperties.class.getDeclaredFields();
//        log.info("fields:{}",fields);
//        for (Field field : fields) {
//            log.info("Field:{}",field);
//            log.info("fieldName:{}",field.getName());
//            String t = cp.getProperty(field.getName());
//            if (t != null){
//                log.info("配置文件中存在值，覆盖中.field:{}",field);
//                Method m = getMethod(field);
//                setField(field,t,m,dp);
//            }
//        }
//        log.info("properties:{}",dp);
//        log.info("开始初始化 Consumer 模块环境");
//        return new ConsumerContext((byte)1);
        return new ConsumerContext((byte)1);
    }
    private static void setField(Field field,String value,Method method,Object obj) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = field.getType();
        if(type == Byte.class){
            method.invoke(obj,Byte.valueOf(value));
        }else if(type == Integer.class){
            method.invoke(obj,Integer.valueOf(value));
        }else if(type == Short.class){
            method.invoke(obj,Short.valueOf(value));
        }else if(type == Long.class){
            method.invoke(obj,Long.valueOf(value));
        }else if(type == String.class){
            Object invoke = method.invoke(obj, value);
        }else {
            throw new IllegalArgumentException("不支持该类型的变量"+type);
        }
    }
    private static Method getMethod(Field field) throws NoSuchMethodException {
        return ZrpcConsumerProperties.class.getMethod("setter"+field.getName().toUpperCase());
    }
}
