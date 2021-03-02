package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.consumer.ConsumerInvoker;
import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import github.zjm404.zrpc.registry.RegistryType;
import github.zjm404.zrpc.test.facade.IDemo;

import java.lang.reflect.Proxy;

/**
 * @author zjm
 * @date 2021/3/2
 */
public class ProxyTest {
    public static void main(String[] args) throws Exception {
        String registryAddr = "127.0.0.1:2181";
        String serviceVersion = "1.0";
        RegistryService registryService = RegistryServiceFactory.getRegistryService(RegistryType.ZOOKEEPER.getCode(), registryAddr);
        IDemo service = (IDemo) Proxy.newProxyInstance(
                IDemo.class.getClassLoader(),
                new Class<?>[]{IDemo.class},
                new ConsumerInvoker(serviceVersion,registryService,5000,(byte)2)
        );
        System.out.println(service.say());
    }
}
