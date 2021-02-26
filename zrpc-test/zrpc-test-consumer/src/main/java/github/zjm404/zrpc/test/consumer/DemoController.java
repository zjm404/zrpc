package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.consumer.ZrpcConsumer;
import github.zjm404.zrpc.test.facade.IDemo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjm
 * @date 2021/2/26
 */
@RestController
public class DemoController {
    @ZrpcConsumer(registryAddr = "127.0.0.1:2181")
    private IDemo demo;

    @GetMapping("/test")
    public String say(){
        return demo.say();
    }
}
