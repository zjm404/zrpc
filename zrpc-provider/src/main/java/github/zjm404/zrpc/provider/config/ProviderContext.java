package github.zjm404.zrpc.provider.config;

import github.zjm404.zrpc.core.ServiceMeta;
import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.ZrpcDecoder;
import github.zjm404.zrpc.protocol.ZrpcEncoder;
import github.zjm404.zrpc.protocol.handler.RequestHandler;
import github.zjm404.zrpc.provider.annotion.ZrpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.imageio.spi.ServiceRegistry;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zjm
 * @date 2020/12/22
 */
@Slf4j
public class ProviderContext implements BeanPostProcessor, InitializingBean {
    private String serviceAddr;
    private int servicePort;
    private ServiceRegistry serviceRegistry;
    private final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    public ProviderContext(String serviceAddr,int servicePort,ServiceRegistry serviceRegistry) {
        this.serviceAddr = serviceAddr;
        this.servicePort = servicePort;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 启动服务
     */
    private void startProviderServer() throws InterruptedException, UnknownHostException {
        this.serviceAddr = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ZrpcDecoder())
                                    .addLast(new RequestHandler(map))
                                    .addLast(new ZrpcEncoder());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = sb.bind(this.serviceAddr, this.servicePort).sync();
            future.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ZrpcService zrpcService = bean.getClass().getAnnotation(ZrpcService.class);
        String serviceName = zrpcService.serviceInterface().getName();
        String serviceVersion = zrpcService.serviceVersion();

        try{
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceAddr(this.serviceAddr);
            serviceMeta.setServicePort(this.servicePort);
            serviceMeta.setServiceName(serviceName);

            serviceRegistry.registerServiceProvider(serviceMeta);
            map.put(ZrpcUtils.buildServiceKey(serviceName,serviceVersion),bean);
            serviceMeta.setServiceVersion(serviceVersion);
        }catch (Exception e){
            e.printStackTrace();
            log.error("注册服务出错,ServiceName:{},ServiceVersion:{},Exception:{}",serviceName,serviceVersion,e);
        }
        return null;
    }
}
