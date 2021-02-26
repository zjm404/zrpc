package github.zjm404.zrpc.consumer.config;

import github.zjm404.zrpc.consumer.ConsumerInvoker;
import github.zjm404.zrpc.consumer.ZrpcConsumer;
import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author
 * @date 2021/2/26
 */
public class ProviderConfig implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;
    private byte serializationCode;
    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ZrpcConsumer.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            ZrpcConsumer annotation = aClass.getAnnotation(ZrpcConsumer.class);
            RegistryService registryService = RegistryServiceFactory.getRegistryService(annotation.registryType(),annotation.registryAddr());
            beanFactory.registerSingleton(aClass.getSimpleName(), Proxy.newProxyInstance(aClass.getClassLoader()
                    ,new Class<?>[]{aClass}
                    ,new ConsumerInvoker(annotation.serviceVersion(),registryService,annotation.timeout(),serializationCode)));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}