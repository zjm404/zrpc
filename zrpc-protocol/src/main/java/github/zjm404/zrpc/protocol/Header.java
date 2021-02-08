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
    private short headerLen;
    private int messageLen;
    private long messageId;
    private byte messageType;
    private byte serializationCode;
}
