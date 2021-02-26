package github.zjm404.zrpc.core;

import java.io.IOException;

/**
 * @Author zjm
 * @Date 2020/11/19
 * @Description
 * @Version 1.0
 */
public interface ISerialization{
    /**
     * 序列化
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[]  enSerialize(T obj) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws IOException
     */
    <T> T deSerialize(byte[] bytes,Class<T> clz) throws IOException;
}
