package github.zjm404.zrpc.protocol;

import github.zjm404.zrpc.core.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义协议
 *
 * @author zjm
 * @date 2021/1/27
 */
@Slf4j
public class ZrpcDecoder extends ByteToMessageDecoder {
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
     * <p>
     * message:
     * byte[] data
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int readableBytes = byteBuf.readableBytes();
        //检测魔数
        short magicNum = byteBuf.readShort();
        //检测版本
        byte version = byteBuf.readByte();
        //消息头长度
        short headerLen = byteBuf.readShort();
        //检测消息长度
        int msgLen = byteBuf.readInt();
        if (isLegalMsg(magicNum, version, msgLen, readableBytes)) {

            //消息id
            long msgId = byteBuf.readLong();
            //消息类型
            byte msgType = byteBuf.readByte();
            //序列化类型
            byte serializationCode = byteBuf.readByte();
            //2+1+4+8+1
            byte[] data = new byte[msgLen - headerLen];
            ISerialization serialization = SerializationFactory.getSerialization(serializationCode);

            //消息头
            Header header = new Header();
            header.setMagicNum(magicNum);
            header.setVersion(version);
            header.setHeaderSize(headerLen);
            header.setMsgSize(msgLen);
            header.setMsgId(msgId);
            header.setMsgType(msgType);
            header.setSerializationCode(serializationCode);

            //根据消息类型采取不同的转化
            if (MessageType.REQUEST.getCode() == msgType) {
                Request request = serialization.deSerialize(data);
                if (request == null) {
                    log.warn("请求消息反序列化后为 null,msgId:{}", msgId);
                    return;
                }
                Message<Request> msg = new Message<>();
                msg.setHeader(header);
                msg.setBody(request);
                list.add(msg);

            } else if (MessageType.RESPONSE.getCode() == msgType) {
                Response response = new Response();
                Object msgBody = serialization.deSerialize(data);
                response.setData(msgBody);
                Message<Response> msg = new Message<>();
                msg.setHeader(header);
                msg.setBody(response);
                list.add(msg);
            } else if (MessageType.HEARTBEAT.getCode() == msgType) {
                //TODO 心跳
            } else {
                log.warn("无该消息类型，MessageType:{}", msgType);
            }
        }
    }

    /**
     * 检测是否是合法消息
     * 魔数检测
     * 长度检测
     */
    private boolean isLegalMsg(short magicNum, byte version, int msgLen, int readableBytes) {
        if (magicNum != ProtocolVersionOne.MAGIC) {
            return false;
        }
        if (version != ProtocolVersionOne.VERSION) {
            return false;
        }
        if (msgLen > readableBytes) {
            return false;
        }
        return true;
    }
}
