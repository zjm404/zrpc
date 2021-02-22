package github.zjm404.zrpc.core.example;

import github.zjm404.zrpc.core.api.IDemo;
import github.zjm404.zrpc.provider.annotion.ZrpcService;

/**
 * @author zjm
 * @date 2020/11/6
 */
@ZrpcService
public class DemoImpl implements IDemo {
    @Override
    public String sayHello() {
        return "hello world";
    }
}
