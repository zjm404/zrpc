package github.zjm404.zrpc.test.consumer;

import github.zjm404.zrpc.consumer.ZrpcConsumer;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author
 * @date 2021/3/2
 */
@Slf4j
public class ReflectionsTest {
    public static void main(String[] args) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("")
                .addScanners(new FieldAnnotationsScanner()));
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(ZrpcConsumer.class);
        log.info("fieldsAnnotatedWith:{}",fieldsAnnotatedWith);
    }
}
