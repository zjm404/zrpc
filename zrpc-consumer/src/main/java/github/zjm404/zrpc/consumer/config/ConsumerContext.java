package github.zjm404.zrpc.consumer.config;

import github.zjm404.zrpc.consumer.ConsumerUtil;
import github.zjm404.zrpc.consumer.ZrpcConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zjm
 * @date 2021/2/3
 */
@Slf4j
public class ConsumerContext implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private byte serializationCode;
    private ApplicationContext context;
    private ClassLoader classLoader;
    private final Map<String, BeanDefinition> serviceBeanDefinitions = new LinkedHashMap<>();
    public ConsumerContext(byte serializationCode){
        log.info("创建ConsumerContext,serializationCode:{}",serializationCode);
        this.serializationCode = serializationCode;
        log.info("创建ConsumerContext,serializationCode:{}",serializationCode);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("Consumer 模块设置 context");
        this.context = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info("客户端启动中...扫描待消费服务的属性");
        for (String beanDefinitionName : configurableListableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(beanClassName != null){
                Class<?> clz = ClassUtils.resolveClassName(beanClassName,this.classLoader);
                ReflectionUtils.doWithFields(clz,this::parseService);
            }
        }

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) configurableListableBeanFactory;
        this.serviceBeanDefinitions.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }
            registry.registerBeanDefinition(beanName, serviceBeanDefinitions.get(beanName));
            log.info("registered RpcReferenceBean {} success.", beanName);
            log.info("bean:{}",serviceBeanDefinitions.get(beanName));
        });

//        //添加属性注解扫描工具
//        Reflections reflections = new Reflections(new ConfigurationBuilder().addScanners(new FieldAnnotationsScanner()));
//        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
//        //获取带有注解的属性
//        Set<Field> typesAnnotatedWith = reflections.getFieldsAnnotatedWith(ZrpcConsumer.class);
//        for (Field aClass : typesAnnotatedWith) {
//            ZrpcConsumer annotation = aClass.getAnnotation(ZrpcConsumer.class);
//            log.info("扫描到注解，ZrpcConsumer:{}",annotation);
//            RegistryService registryService = RegistryServiceFactory.getRegistryService(annotation.registryType(),annotation.registryAddr());
//            beanFactory.registerSingleton(aClass.getType().getSimpleName(), Proxy.newProxyInstance(aClass.getType().getClassLoader()
//                    ,new Class<?>[]{aClass.getType()}
//                    ,new ConsumerInvoker(annotation.serviceVersion(),registryService,annotation.timeout(),serializationCode)));
//        }

    }
    private void parseService(Field field){
        ZrpcConsumer annotation = AnnotationUtils.getAnnotation(field,ZrpcConsumer.class);
        if (annotation != null){
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(ConsumerUtil.getInitMethodName());
            builder.addPropertyValue("interfaceClass",field.getType());
            builder.addPropertyValue("serviceVersion",annotation.serviceVersion());
            builder.addPropertyValue("registryType",annotation.registryType());
            builder.addPropertyValue("registryAddr",annotation.registryAddr());
            builder.addPropertyValue("timeout",annotation.timeout());
            builder.addPropertyValue("serializationCode",serializationCode);
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            serviceBeanDefinitions.put(field.getName(), beanDefinition);
        }
    }
}
