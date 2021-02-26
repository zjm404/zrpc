package github.zjm404.zrpc.provider;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2021/2/25
 */
public class RequestExecutor {
    private static ThreadPoolExecutor threadPoolExecutor;
    public static void submitRequest(Runnable task){
        if(threadPoolExecutor == null){
            synchronized (RequestExecutor.class){
                if(threadPoolExecutor == null){
                    TimeUnit unit;
                    BlockingQueue workQueue;
                    threadPoolExecutor = new ThreadPoolExecutor(6, 6, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
