package github.zjm404.zrpc.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

/**
 * 通过 rpc 获取到的 bean
 * @author zjm
 * @date 2021/2/4
 */
@Getter
@Setter
public class RpcReferenceBean implements FactoryBean<Object> {
    private String registryType;
    private Class<?> interfaceClass;
    private String serviceAddr;
    private String servicePort;
    private String serviceVersion;
    private long timeout;
    private Object service;

    @Override
    public Object getObject() throws Exception {
        return service;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
