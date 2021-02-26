package github.zjm404.zrpc.test.provider;
import github.zjm404.zrpc.provider.annotion.ZrpcService;
import github.zjm404.zrpc.test.facade.IDemo;

import java.io.Serializable;

/**
 * @author zjm
 * @date 2021/2/26
 */
@ZrpcService(serviceInterface = DemoImpl.class)
public class DemoImpl implements IDemo, Serializable {
    @Override
    public String say() {
        return "hello";
    }
}
