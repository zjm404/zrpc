package github.zjm404.zrpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zjm
 * @date 2021/1/27
 */
@Data
public class Response implements Serializable {
    private Object data;
    /**
     * 响应的附加信息
     */
    private String msg;
}
