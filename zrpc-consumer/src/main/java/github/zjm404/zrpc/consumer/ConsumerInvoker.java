package github.zjm404.zrpc.consumer;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author zjm
 * @Date 2020/11/5
 * @Description
 * @Version 1.0
 */
@Slf4j
public class ConsumerInvoker implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //TODO: 访问注册中心，获取服务地址
        //TODO：向服务节点发送服务请求
    }
}
