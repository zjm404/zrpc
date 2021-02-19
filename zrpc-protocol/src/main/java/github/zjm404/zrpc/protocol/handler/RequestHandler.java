package github.zjm404.zrpc.protocol.handler;

import github.zjm404.zrpc.core.ISerialization;
import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zjm
 * @date 2021/2/2
 */
public class RequestHandler extends SimpleChannelInboundHandler<Message<Request>> {

    private ConcurrentHashMap<String, Object> map;
    public RequestHandler(ConcurrentHashMap<String,Object> map){
        this.map = map;
    }

    /**
     * 处理请求
     * 这里应该是使用配置文件的形式更好
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Request> msg) throws Exception {
        Header header = msg.getHeader();
        Request request = msg.getBody();
        String key = ZrpcUtils.buildServiceKey(request.getServiceName(),request.getServiceVersion());
        Object obj = map.get(key);
        Response response = new Response();
        Message<Response> message = new Message<>();
        if(obj != null){
            ISerialization serialization = SerializationFactory.getSerialization(header.getSerializationCode());
            response.setData(serialization.enSerialize(obj));
            header.setMsgType(MessageType.RESPONSE.getCode());
            message.setHeader(header);
            message.setBody(response);
        }else{

        }
        ctx.writeAndFlush(message);
    }
}
