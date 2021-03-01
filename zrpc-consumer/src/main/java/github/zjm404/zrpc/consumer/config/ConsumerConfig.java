package github.zjm404.zrpc.consumer.config;

import github.zjm404.zrpc.consumer.ConsumerInvoker;
import github.zjm404.zrpc.consumer.ZrpcConsumer;
import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author zjm
 * @date 2021/2/26
 */
@Slf4j
public class ConsumerConfig implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;
    private byte serializationCode;
    public ConsumerConfig(byte serializationCode){
        this.serializationCode = serializationCode;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addScanners(new FieldAnnotationsScanner()));
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(ZrpcConsumer.class);
        log.info("fieldsAnnotatedWith:{}",fieldsAnnotatedWith);
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            if(beanDefinition != null){

            }
        }
//        for (Field field : fieldsAnnotatedWith) {
//            ZrpcConsumer annotation = field.getAnnotation(ZrpcConsumer.class);
//            RegistryService registryService = RegistryServiceFactory.getRegistryService(annotation.registryType(),annotation.registryAddr());
//            beanFactory.registerSingleton(field.getType().getName(), Proxy.newProxyInstance(field.getType().getClassLoader()
//                    ,new Class<?>[]{field.getType()}
//                    ,new ConsumerInvoker(annotation.serviceVersion(),registryService,annotation.timeout(),serializationCode)));
//        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
