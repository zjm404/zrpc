package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.protocol.Message;
import github.zjm404.zrpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zjm
 * @date 2021/2/2
 */
public class ResponseHandler extends SimpleChannelInboundHandler<Message<Response>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Response> responseMessage) throws Exception {
        ZrpcFuture<Response> future = ConsumerUtil.getResponse(responseMessage.getHeader().getMsgId());
        future.getPromise().setSuccess(responseMessage.getBody());
    }
}
