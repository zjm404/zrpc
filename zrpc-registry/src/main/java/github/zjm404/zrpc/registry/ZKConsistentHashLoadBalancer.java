package github.zjm404.zrpc.registry;

import github.zjm404.zrpc.core.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * 一致性 hash 算法
 * @author zjm
 * @date 2021/1/27
 */
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {
    /**
     *
     * @param servers
     * @param hashCode
     * @return
     */
    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
        return null;
    }
}
