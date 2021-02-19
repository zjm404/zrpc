package github.zjm404.zrpc.consumer;

import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author zjm
 */
@AllArgsConstructor
@Getter
public class ZrpcFuture<T> {
    private int timeout;
    private Promise<T> promise;
}
