package github.zjm404.zrpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 网络通信时的消息
 * @author zjm
 * @date 2021/1/27
 */
@Data
public class Message<T> implements Serializable {
    private Header header;
    private T body;
}
