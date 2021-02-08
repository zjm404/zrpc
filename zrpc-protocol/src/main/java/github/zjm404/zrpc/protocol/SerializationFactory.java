package github.zjm404.zrpc.protocol;


import github.zjm404.zrpc.core.ISerialization;

/**
 * @author zjm
 * @date 2021/1/28
 */
public class SerializationFactory {
    public static ISerialization getSerialization(byte code){
         //TODO:这里可以做扩展，将配置文件中的序列化插件读取出来，然后通过反射创建
        return new JdkSerialization();
    }
}
