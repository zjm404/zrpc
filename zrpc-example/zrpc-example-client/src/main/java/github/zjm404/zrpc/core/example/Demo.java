package github.zjm404.zrpc.core.example;

import github.zjm404.zrpc.consumer.ZrpcConsumer;
import github.zjm404.zrpc.core.api.IDemo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjm
 * @date 2021/2/22
 */
@RestController
public class Demo {
    @ZrpcConsumer
    private IDemo iDemo;
    @GetMapping("/test")
    public String doWork(){
        return iDemo.sayHello();
    }
}
