package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.protocol.Message;
import github.zjm404.zrpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zjm
 * @date 2021/2/2
 */
@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<Message<Response>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Response> responseMessage) throws Exception {
        log.info("接收响应,response:{}",responseMessage);
        ZrpcFuture<Response> future = ConsumerUtil.getResponse(responseMessage.getHeader().getMsgId());
        future.getPromise().setSuccess(responseMessage.getBody());
    }
}
