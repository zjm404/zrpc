package github.zjm404.zrpc.core.example;

import java.util.ServiceLoader;

/**
 * @Author
 * @Date 2020/11/6
 * @Description
 * @Version 1.0
 */
//@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
//        SpringApplication.run(ServerApplication.class,args);
        IServer server = ServiceLoader.load(IServer.class).iterator().next();
        System.out.println("hello world");
//        server.start("hello world".getBytes());
    }
}
