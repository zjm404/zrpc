package github.zjm404.zrpc.registry;

import github.zjm404.zrpc.core.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性 hash 算法
 * @author zjm
 * @date 2021/1/27
 */
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {
    public final static int VIRTUAL_NODE = 5;
    /**
     *
     * @param servers
     * @param hashCode
     * @return
     */
    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
        //创建 Hash 环
        TreeMap<Integer,ServiceInstance<ServiceMeta>> hashCycle = new TreeMap<>();
        for (ServiceInstance<ServiceMeta> server : servers) {
            //创建虚拟节点，分散流量
            for (int i = 0; i < VIRTUAL_NODE; i++) {
                hashCycle.put((buildKey(server)+i).hashCode(),server);
            }
        }
        //选择节点
        //查找第一个比客户端hashcode大的节点
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> res = hashCycle.ceilingEntry(hashCode);
        if(res == null){
            //如果没有比客户端 hashcode 大的节点，则获取第一个节点
            res = hashCycle.firstEntry();
        }
        return res.getValue();
    }
    private String buildKey(ServiceInstance<ServiceMeta> server){
        ServiceMeta payload = server.getPayload();
        return payload.getServiceAddr()+":"+String.valueOf(payload.getServicePort());
    }
}
