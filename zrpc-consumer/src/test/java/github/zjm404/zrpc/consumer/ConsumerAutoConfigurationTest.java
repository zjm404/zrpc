package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.consumer.config.ConsumerAutoConfiguration;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author
 * @date 2021/2/26
 */
public class ConsumerAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

    @After
    public void destroy(){
        if(this.context != null){
            context.close();
        }
    }
    @Test
    public void testAll(){
        load(ConsumerAutoConfiguration.class);
    }

    private void load(Class<?> config) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(config);
        applicationContext.refresh();
        this.context = applicationContext;
    }
}
