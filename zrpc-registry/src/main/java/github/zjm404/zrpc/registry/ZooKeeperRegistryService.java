package github.zjm404.zrpc.registry;

import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.core.ServiceMeta;
import github.zjm404.zrpc.core.ZrpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author zjm
 * @date 2021/1/27
 */
@Slf4j
public class ZooKeeperRegistryService implements RegistryService {
    private static final int BASE_SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_BASE_PATH = "/zrpc";

    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZooKeeperRegistryService(String registryAddr) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS,MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(ZrpcUtils.buildServiceKey(serviceMeta.getServiceName(),serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
        log.info("注册服务成功，serviceMeta:{}",serviceMeta);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        if(serviceMeta == null){
            log.warn("注册中心取消注册服务失败，serviceMeta 为 null");
            return;
        }
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(ZrpcUtils.buildServiceKey(serviceMeta.getServiceName(),serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMeta discover(String serviceName,int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        //使用负载均衡算法选择合适的服务器
        ServiceInstance<ServiceMeta> serviceInstance = new ZKConsistentHashLoadBalancer().select((List<ServiceInstance<ServiceMeta>>) serviceInstances,invokerHashCode);
        if(serviceInstance != null){
            return serviceInstance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
