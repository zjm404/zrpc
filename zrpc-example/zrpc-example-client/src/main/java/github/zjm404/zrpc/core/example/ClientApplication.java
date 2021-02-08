package github.zjm404.zrpc.core.example;

import github.zjm404.zrpc.api.IDemo;
import github.zjm404.zrpc.bootstrap.proxy.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author
 * @Date 2020/11/5
 * @Description
 * @Version 1.0
 */
@SpringBootApplication
public class ClientApplication {
    @Consumer
    private static IDemo demo;
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class);
        demo = (IDemo) context.getBean("consumer");
        demo.sayHello();
     }
}
