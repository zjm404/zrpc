package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.core.ServiceMeta;
import github.zjm404.zrpc.protocol.Message;
import github.zjm404.zrpc.protocol.Request;
import github.zjm404.zrpc.protocol.ZrpcDecoder;
import github.zjm404.zrpc.protocol.ZrpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerTransport {
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    public ConsumerTransport(){
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(4);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ZrpcEncoder())
                                .addLast(new ZrpcDecoder())
                                .addLast(new ResponseHandler());
                    }
                });
    }

    public void sendRequest(Message<Request> msg, ServiceMeta serviceMeta) throws InterruptedException {
        if(serviceMeta == null){
            log.warn("connect service provider error,serviceMeta is null");
            return;
        }
        ChannelFuture future = bootstrap.connect(serviceMeta.getServiceAddr(),serviceMeta.getServicePort()).sync();
        future.addListener((ChannelFutureListener)arg0->{
            if(future.isSuccess()){
                log.info("connect service provider success, addr: {},port: {}",serviceMeta.getServiceAddr(),serviceMeta.getServicePort());
            }else{
                log.error("connect service provider failed,addr: {},port: {}",serviceMeta.getServiceAddr(),serviceMeta.getServicePort());
                future.cause().printStackTrace();
                group.shutdownGracefully();
            }
        });
        future.channel().writeAndFlush(msg);
    }
}
