package github.zjm404.zrpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zjm
 * @date 2021/1/27
 */
@Data
public class Request implements Serializable {
    /**
     * 请求的 Service 类名
     */
    private String serviceName;
    /**
     * 请求的 Service 版本
     */
    private String serviceVersion;
    /**
     * 请求的方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] argTypes;
    /**
     * 参数
     */
    private Object[] args;
}
