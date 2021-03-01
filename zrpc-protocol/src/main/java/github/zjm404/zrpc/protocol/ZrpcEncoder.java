package github.zjm404.zrpc.protocol;

import github.zjm404.zrpc.core.ISerialization;
import github.zjm404.zrpc.protocol.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息如下：
 * header:
 * short magicNum;           2byte
 * byte version;             1byte
 * short headerLen;          2byte
 * int messageLen;           4byte
 * long messageId;           8byte
 * byte messageType;         1byte
 * byte serializationCode;   1byte
 * //以后会增加扩展参数
 *
 * message:
 * byte[] data
 *
 * @author zjm
 * @date 2021/1/27
 */
@Slf4j
public class ZrpcEncoder extends MessageToByteEncoder<Message<Object>> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message<Object> objectMessage, ByteBuf byteBuf) throws Exception {
        log.info("开始消息编码");
        Header header = objectMessage.getHeader();
        log.info("编码消息，消息为:{}", objectMessage);
        //魔数
        byteBuf.writeShort(header.getMagicNum());
        //消息协议版本
        byteBuf.writeByte(header.getVersion());
        //消息头长度（以后要是使用附加字段的话，这里再修改
        byteBuf.writeShort(header.getHeaderSize());
        int msgSize = header.getHeaderSize();
        byte[] data = new byte[0];
        if (objectMessage.getBody() != null) {
            ISerialization serialization = SerializationFactory.getSerialization(header.getSerializationCode());
            data = serialization.enSerialize(objectMessage.getBody());
            msgSize += data.length;
        }
        //消息长度
        byteBuf.writeInt(msgSize);
        //消息ID
        byteBuf.writeLong(header.getMsgId());
        //消息类型
        byteBuf.writeByte(header.getMsgType());
        //序列化协议
        byteBuf.writeByte(header.getSerializationCode());

        if(data.length != 0){
            //写入消息
            log.info("写入了消息,size:{}",data.length);
            byteBuf.writeBytes(data);
        }
    }

}
