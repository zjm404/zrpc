package github.zjm404.zrpc.core;

/**
 * @author zjm
 * @date 2021/1/27
 */
public class ZrpcUtils {
    public static String buildServiceKey(String serviceName,String serviceVersion){
        return String.join("#",serviceName,serviceVersion);
    }
}
