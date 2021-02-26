package github.zjm404.zrpc.protocol.serialization;

import github.zjm404.zrpc.core.ISerialization;

import java.io.IOException;

/**
 * @author
 * @date 2021/2/25
 */
public class JsonSerialization implements ISerialization {

    @Override
    public <T> byte[] enSerialize(T obj) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clz) throws IOException {
        return null;
    }
}
