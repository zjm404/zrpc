package github.zjm404.zrpc.protocol;

import lombok.Data;

/**
 * @author zjm
 * @date 2021/1/27
 */
@Data
public class Header{
    private short magicNum;
    private byte version;
    private short headerSize;
    private int msgSize;
    private long msgId;
    private byte msgType;
    private byte serializationCode;
    /**
     * 作为扩展字段使用
     */
    private String[] extension;
}
