package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.consumer.ConsumerTransport;
import github.zjm404.zrpc.consumer.ConsumerUtil;
import github.zjm404.zrpc.consumer.ZrpcFuture;
import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.core.ServiceMeta;
import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.*;
import github.zjm404.zrpc.protocol.serialization.SerializationEnum;
import github.zjm404.zrpc.registry.RegistryServiceFactory;
import github.zjm404.zrpc.registry.RegistryType;
import github.zjm404.zrpc.test.facade.IDemo;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zjm
 * @date 2021/3/1
 */
@Slf4j
public class TransportTest {
    public static void main(String[] args) throws Exception {
        ConsumerTransport transport = new ConsumerTransport();

        //组装信息
        Header header = new Header();
        long requestId = ConsumerUtil.getMsgId();
        header.setMagicNum(ProtocolVersionOne.MAGIC);
        header.setVersion(ProtocolVersionOne.VERSION);
        header.setMsgId(requestId);
        header.setHeaderSize(ProtocolVersionOne.HEADER_SIZE_WITHOUT_EXTENSION);
        header.setMsgType(MessageType.REQUEST.getCode());
        header.setSerializationCode(SerializationEnum.HESSIAN.getCode());

        Request request = new Request();
        request.setServiceName(IDemo.class.getName());
        log.info("serviceName:{}",request.getServiceName());
        request.setServiceVersion("1.0");

        Message<Request> msg = new Message<>();
        msg.setBody(request);
        msg.setHeader(header);
        //通过注册中心获取 ServiceMeta
        String registryAddr = "127.0.0.1:2181";
        RegistryService registry = RegistryServiceFactory.getRegistryService(RegistryType.ZOOKEEPER.getCode(), registryAddr);
        String serviceName = ZrpcUtils.buildServiceKey(request.getServiceName(),request.getServiceVersion());
        ServiceMeta serviceMeta = registry.discover(serviceName,serviceName.hashCode());
        log.info("serviceMeta:{}",serviceMeta);
        int timeout = 5000;
        ZrpcFuture<Response> future = new ZrpcFuture<>(
                timeout
                ,new DefaultPromise<>(new DefaultEventLoop())
        );
        ConsumerTransport consumerTransport = new ConsumerTransport();
        consumerTransport.sendRequest(msg,serviceMeta);
        Object data = future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
        System.out.println(data);
    }
}
