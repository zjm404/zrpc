package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.core.RegistryService;
import github.zjm404.zrpc.core.ServiceMeta;
import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.*;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 动态代理，实现获取服务
 * @author zjm
 * @date 2020/11/5
 */
@Slf4j
public class ConsumerInvoker implements InvocationHandler {
    private String serviceVersion;
    private RegistryService registryService;
    private int timeout;
    private ConsumerTransport consumerTransport;
    public ConsumerInvoker(String serviceVersion,RegistryService registryService,int timeout){
        this.serviceVersion = serviceVersion;
        this.registryService = registryService;
        this.timeout = timeout;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Message<Request> msg = new Message<>();
        //创建请求
        Header header = new Header();
        long requestId = ConsumerUtil.getMsgId();
        header.setMagicNum(ProtocolVersionOne.MAGIC);
        header.setMsgId(requestId);
        header.setHeaderSize(ProtocolVersionOne.HEADER_SIZE_WITHOUT_EXTENSION);
        header.setMsgType(MessageType.REQUEST.getCode());
        header.setSerializationCode(SerializationEnum.JDK.getCode());

        Request request = new Request();
        request.setServiceVersion(serviceVersion);
        request.setServiceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setArgs(args);
        request.setArgTypes(method.getParameterTypes());

        msg.setHeader(header);
        msg.setBody(request);
        //访问注册中心，获取服务地址
        String registryKey = ZrpcUtils.buildServiceKey(request.getServiceName(),serviceVersion);
        int registryHashCode = registryKey.hashCode();
        ServiceMeta serviceMeta = registryService.discover(registryKey, registryHashCode);
        //向服务节点发送服务请求
        ZrpcFuture<Response> future = new ZrpcFuture<>(
                timeout
                ,new DefaultPromise<>(new DefaultEventLoop())
        );
        ConsumerTransport consumerTransport = new ConsumerTransport();
        consumerTransport.sendRequest(msg,serviceMeta);
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
