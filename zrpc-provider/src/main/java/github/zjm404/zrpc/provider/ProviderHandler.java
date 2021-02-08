package github.zjm404.zrpc.provider;

import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.Header;
import github.zjm404.zrpc.protocol.Message;
import github.zjm404.zrpc.protocol.MessageType;
import github.zjm404.zrpc.protocol.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zjm
 * @date 2021/2/1
 */
@Slf4j
public class ProviderHandler extends SimpleChannelInboundHandler<Message<Request>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Request> msg) throws Exception {
        Request request = msg.getBody();
        if(request == null){
            log.error("请求消息body为null,msgId:{}",msg.getHeader().getMessageId());
            return;
        }
        Header header = msg.getHeader();
        header.setMessageType(MessageType.REQUEST.getCode());
        String key = ZrpcUtils.buildServiceKey(request.getServiceName(),request.getServiceVersion());
    }
}
