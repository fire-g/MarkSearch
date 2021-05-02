package pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author haotian
 */
public class Pool {

    /**
     * 线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(
            5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());

    public static void shutdown(){
        EXECUTOR_SERVICE.shutdown();
    }

    public static void execute(Runnable runnable){
        EXECUTOR_SERVICE.execute(runnable);
    }
}
