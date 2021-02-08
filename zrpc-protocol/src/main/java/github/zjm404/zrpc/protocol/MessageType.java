package github.zjm404.zrpc.protocol;

import lombok.Getter;

/**
 * @author zjm
 * @date 2021/1/27
 */
public enum MessageType {
    /**
     * 请求
     */
    REQUEST((byte)1),
    /**
     * 响应
     */
    RESPONSE((byte)2),
    /**
     * 心跳
     */
    HEARTBEAT((byte)3);
    @Getter
    private final byte code;

    MessageType(byte i) {
        this.code = i;
    }
}
