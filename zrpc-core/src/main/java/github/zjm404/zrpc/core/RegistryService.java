package github.zjm404.zrpc.core;

import java.io.IOException;

/**
 * @author zjm
 * @date 2021/1/27
 */
public interface RegistryService {
    /**
     * 注册服务
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 注销服务
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 发现服务
     * @param serviceName
     * @throws Exception
     */
    ServiceMeta discover(String serviceName,int invokerHashCode) throws Exception;

    /**
     * 销毁注册中心
     */
    void destroy() throws IOException;
}
