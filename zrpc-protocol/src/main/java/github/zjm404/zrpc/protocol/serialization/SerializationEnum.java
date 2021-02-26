package github.zjm404.zrpc.protocol.serialization;

import lombok.Getter;

public enum SerializationEnum {
    /**
     * 使用 JDK 原生序列化
     */
    JDK((byte)1),

    HESSIAN((byte)2);

    @Getter
    private final byte code;
    SerializationEnum(byte code) {
        this.code = code;
    }
}
