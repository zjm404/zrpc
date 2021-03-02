package github.zjm404.zrpc.provider;

import github.zjm404.zrpc.core.ZrpcUtils;
import github.zjm404.zrpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
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
            if(obj == null){
                throw new NullPointerException(String.format("不存在该service,服务名称：%s 服务版本：%s",request.getServiceName(),request.getServiceVersion()));
            }

            Object data = executeServiceMethod(obj,request);
            Response response = new Response();
            Message<Response> message = new Message<>();
            response.setData(data);
            log.info("响应拼接完成，response:{}",response);
            header.setMsgType(MessageType.RESPONSE.getCode());
            message.setHeader(header);
            message.setBody(response);
            ctx.writeAndFlush(message);
            log.info("写入消息:{}",message);
        });
    }

    /**
     * 执行方法，返回执行后的返回值
     * @return
     */
    private Object executeServiceMethod(Object service,Request request){
        log.info("读取到消息请求,request:{}",request);
        Class<?> aClass = service.getClass();
        String methodName = request.getMethodName();
        Class<?>[] argTypes = request.getArgTypes();
        Object[] args = request.getArgs();
        log.info("开始拼接响应");

        FastClass fastClass = FastClass.create(aClass);
        FastMethod fastMethod = fastClass.getMethod(methodName,argTypes);
        Object data = null;
        try {
            log.info("进行 fastClass");
            data = fastMethod.invoke(service,args);
        } catch (InvocationTargetException e) {
            log.info("fastClass报错,StackTrace:{}", (Object) e.getStackTrace());
        }
        return data;
    }
}
