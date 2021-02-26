package github.zjm404.zrpc.protocol;

import github.zjm404.zrpc.core.ISerialization;
import github.zjm404.zrpc.protocol.serialization.SerializationFactory;
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
        byteBuf.markReaderIndex();
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes < 9) {
//            log.warn("消息解码失败，消息过短。消息长度:{}", readableBytes);
            return;
        }

        //魔数
        short magicNum = byteBuf.readShort();
        if(magicNum != ProtocolVersionOne.MAGIC){
//            log.warn("消息解码失败,非法消息，magicNum:{}", magicNum);
            return;
        }
        //消息协议版本
        byte version = byteBuf.readByte();
        if (version != ProtocolVersionOne.VERSION) {
//            log.warn("消息解码失败，当前消息协议不支持该版本.version:{}", version);
            return;
        }
        //消息头长度
        short headerSize = byteBuf.readShort();
        //消息长度
        int msgSize = byteBuf.readInt();
        if (msgSize > readableBytes) {
//            log.warn("消息解码失败，消息长度过短，size:{}", msgSize);
            byteBuf.resetReaderIndex();
            return;
        }

        //消息ID
        long msgId = byteBuf.readLong();
        //消息类型
        byte msgType = byteBuf.readByte();
        //序列化协议
        byte serializationCode = byteBuf.readByte();
        log.info("msgLen:{},headerLen:{}", msgSize, headerSize);
        //2+1+4+8+1
        byte[] data = new byte[msgSize - headerSize];
        byteBuf.readBytes(data);
        ISerialization serialization = SerializationFactory.getSerialization(serializationCode);

        //消息头
        Header header = new Header();
        header.setMagicNum(magicNum);
        header.setVersion(version);
        header.setHeaderSize(headerSize);
        header.setMsgSize(msgSize);
        header.setMsgId(msgId);
        header.setMsgType(msgType);
        header.setSerializationCode(serializationCode);

        //根据消息类型采取不同的转化
        if (MessageType.REQUEST.getCode() == msgType) {
            log.info("Data:{}",data);
            if(data.length == 0){
                return;
            }
            Request request = serialization.deSerialize(data,Request.class);
            if (request == null) {
                log.info("header:{}",header);
                log.warn("请求消息反序列化后为 null,msgId:{}", msgId);
                return;
            }
            Message<Request> msg = new Message<>();
            msg.setHeader(header);
            msg.setBody(request);
            list.add(msg);

        } else if (MessageType.RESPONSE.getCode() == msgType) {
            log.info("data:{}",data);
            Response response = serialization.deSerialize(data,Response.class);
            Message<Response> msg = new Message<>();
            msg.setHeader(header);
            msg.setBody(response);
            log.info("消息解析成功，response:{}",response);
            list.add(msg);
        } else if (MessageType.HEARTBEAT.getCode() == msgType) {
            //TODO 心跳
        } else {
            log.warn("无该消息类型，MessageType:{}", msgType);
        }
    }
}
