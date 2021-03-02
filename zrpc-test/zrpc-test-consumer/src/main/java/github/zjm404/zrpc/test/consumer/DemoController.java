package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.consumer.ZrpcConsumer;
import github.zjm404.zrpc.test.facade.IDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zjm
 * @date 2021/2/26
 */
@RestController
public class DemoController {
    @ZrpcConsumer
    @Resource(type = IDemo.class)
    private IDemo demo;

    @GetMapping("/test")
    public String say(){
//        return "hello world";
        return demo.say();
    }
}
