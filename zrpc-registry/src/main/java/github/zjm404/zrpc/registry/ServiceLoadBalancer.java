package github.zjm404.zrpc.registry;

import java.util.List;

/**
 * @author
 * @date 2021/1/27
 */
public interface ServiceLoadBalancer<T> {
    T select(List<T> servers,int hashCode);
}
