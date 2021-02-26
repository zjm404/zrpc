package github.zjm404.zrpc.registry;

import lombok.Getter;

import java.rmi.registry.Registry;

/**
 * @author
 * @date 2021/2/23
 */
public enum RegistryType {

    ZOOKEEPER((byte)1);

    @Getter
    private final byte code;

    RegistryType(byte code){
        this.code = code;
    }
}
