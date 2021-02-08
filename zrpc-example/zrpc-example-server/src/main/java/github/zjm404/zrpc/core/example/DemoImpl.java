package github.zjm404.zrpc.core.example;

import github.zjm404.zrpc.core.api.IDemo;
import github.zjm404.zrpc.core.common.annotation.Provider;

/**
 * @Author
 * @Date 2020/11/6
 * @Description
 * @Version 1.0
 */
@Provider
public class DemoImpl implements IDemo {
    @Override
    public void sayHello() {
        System.out.println("hello world");
    }
}
