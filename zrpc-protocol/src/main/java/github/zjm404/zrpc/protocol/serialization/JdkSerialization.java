package github.zjm404.zrpc.protocol.serialization;

import github.zjm404.zrpc.core.ISerialization;

import java.io.*;

/**
 * 使用Jdk序列化框架实现序列化与反序列化
 * @author  zjm
 * @date 2020/11/4
 */
public class JdkSerialization implements ISerialization {
    @Override
    public <T> byte[] enSerialize(T obj) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        try(ObjectOutputStream oo = new ObjectOutputStream(bao)) {
            oo.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bao.toByteArray();
    }

    public <T> T deSerialize(byte[] bytes, Class<T> clz) {
        try(ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
