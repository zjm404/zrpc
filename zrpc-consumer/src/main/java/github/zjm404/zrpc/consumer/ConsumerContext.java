package github.zjm404.zrpc.consumer;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author zjm
 * @date 2021/2/3
 */
@Component
@Slf4j
public class ConsumerContext implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ZrpcConsumer.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            beanFactory.registerSingleton(aClass.getSimpleName(), Proxy.newProxyInstance(aClass.getClassLoader(),new Class<?>[]{aClass},new ConsumerInvoker()));
        }
    }
}
