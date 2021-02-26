package github.zjm404.zrpc.protocol.serialization;


import github.zjm404.zrpc.core.ISerialization;

/**
 * @author zjm
 * @date 2021/1/28
 */
public class SerializationFactory {
    public static ISerialization getSerialization(byte code){
        if(code == SerializationEnum.HESSIAN.getCode()){
            return new HessianSerialization();
        }else if(code == SerializationEnum.JDK.getCode()){
            return new JdkSerialization();
        }else{
            throw new IllegalArgumentException();
        }
    }
}
