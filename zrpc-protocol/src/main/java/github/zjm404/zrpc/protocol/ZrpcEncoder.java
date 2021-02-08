package github.zjm404.zrpc.protocol;

import github.zjm404.zrpc.core.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

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
public class ZrpcEncoder extends MessageToByteEncoder<Message<Object>> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message<Object> objectMessage, ByteBuf byteBuf) throws Exception {
        Header header = objectMessage.getHeader();
        //魔数
        byteBuf.writeShort(header.getMagicNum());
        //消息协议版本
        byteBuf.writeByte(header.getVersion());
        //消息头长度
        byteBuf.writeShort(header.getHeaderLen());
        //消息长度
        byteBuf.writeLong(header.getMessageLen());
        //消息ID
        byteBuf.writeLong(header.getMessageId());
        //消息类型
        byteBuf.writeByte(header.getMessageType());
        //序列化协议
        byteBuf.writeByte(header.getSerializationCode());

        if (objectMessage.getBody() != null) {
            ISerialization serialization = SerializationFactory.getSerialization(header.getSerializationCode());
            //写入消息
            byteBuf.writeBytes(serialization.enSerialize(objectMessage.getBody()));
        }
    }

}
