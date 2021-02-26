package github.zjm404.zrpc.consumer.config;

import github.zjm404.zrpc.consumer.ConsumerInvoker;
import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * 通过 rpc 获取到的 bean
 * @author zjm
 * @date 2021/2/4
 */
@Getter
@Setter
@Slf4j
public class RpcReferenceBean implements FactoryBean<Object> {
    private byte registryType;
    private Class<?> interfaceClass;
    private String registryAddr;
    private String servicePort;
    private String serviceVersion;
    private byte serializationCode;
    private int timeout;
    private Object service;

    @Override
    public Object getObject() throws Exception {
        return service;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public void init() throws Exception{
        log.info("初始化init...");
        RegistryService registryService = RegistryServiceFactory.getRegistryService(this.registryType,this.registryAddr);
        this.service = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ConsumerInvoker(serviceVersion,registryService,timeout,serializationCode)
        );
        log.info("service:{}",this.service);
    }
}
