package github.zjm404.zrpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zjm
 * @date 2021/1/27
 */
@Data
public class ServiceMeta implements Serializable {
    private String serviceAddr;
    private Integer servicePort;
    private String serviceName;
    private String ServiceVersion;
}
