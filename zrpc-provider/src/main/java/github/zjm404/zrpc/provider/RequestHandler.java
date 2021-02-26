package github.zjm404.zrpc.provider;

import github.zjm404.zrpc.core.ISerialization;
import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zjm
 * @date 2021/2/2
 */
@Slf4j
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
        RequestExecutor.submitRequest(()->{
            Header header = msg.getHeader();
            Request request = msg.getBody();
            String key = ZrpcUtils.buildServiceKey(request.getServiceName(),request.getServiceVersion());
            Object obj = map.get(key);
            Response response = new Response();
            Message<Response> message = new Message<>();
            if(obj != null){
                log.info("读取到消息请求,request:{}",request);
                response.setData(obj);
                header.setMsgType(MessageType.RESPONSE.getCode());
                message.setHeader(header);
                message.setBody(response);
            }else{
                log.warn("读取到消息请求，但不存在该服务。request:{}",request);
            }
            ctx.writeAndFlush(message);
            log.info("写入消息:{}",message);
        });
    }
}
