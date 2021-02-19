package github.zjm404.zrpc.consumer;

import github.zjm404.zrpc.protocol.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerUtil {
    private static final AtomicLong MSG_ID = new AtomicLong(0);
    private static final Map<Long,ZrpcFuture<Response>> RESPONSE_MAP = new ConcurrentHashMap<>();

    public static long getMsgId(){
        return MSG_ID.getAndIncrement();
    }
    public static ZrpcFuture<Response> getResponse(Long msgId){
        if(msgId == null){
            return null;
        }
        return RESPONSE_MAP.remove(msgId);
    }
    public static void addResponse(Long msgId,ZrpcFuture<Response> response){
        RESPONSE_MAP.put(msgId, response);
    }
}
