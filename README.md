# zrpc
简易的 rpc 框架用于学习

技术栈：Java / Netty / SpringBoot /  ZooKeeper / Reflections / 序列化 / 动态代理

功能：

* 基本的服务暴露，服务消费功能

* 使用自定义的消息协议，可扩展
* 使用消息+消息长度解决粘包拆包带来的问题

* 使用注解来标记服务提供者和消费者

* 使用 ZooKeeper 做注册中心，并实现了一致性hash算法进行负载均衡

* 支持自动配置，也可基于配置文件配置一些功能，例如序列化算法，消息协议，注册中心

* 异步消费服务 

使用方式可以见 zrpc-test 模块

项目还有待优化

* 有多余的注释掉的代码

* 有多余的排查问题时留下的日志
* SpringBoot 自动装配还存在问题，没扫描到配置了，改成在启动类上手动添加扫描路径了
* 解决粘包拆包问题时的逻辑还有待优化，遇到错误时应该直接将错误信息返回给消费者，这里还没来得及实现
* 本来想实现个插件化，这里还是搁置了，部分模块的划分还有些乱
* 可能还有其它地方有问题，想起来再天添上吧...

以上问题，有时间我再改改
