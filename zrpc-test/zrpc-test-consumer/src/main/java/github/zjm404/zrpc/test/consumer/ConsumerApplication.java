package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.test.facade.IDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zjm
 * @date 2021/2/26
 */
@SpringBootApplication
@ComponentScan(basePackages = {"github.zjm404.zrpc.consumer.config","github.zjm404.zrpc.test.consumer"})
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }
}
