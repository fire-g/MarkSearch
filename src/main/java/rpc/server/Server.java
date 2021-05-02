package rpc.server;

/**
 * @author haotian
 */
public interface Server {
    void stop();
    void start();
    void register(Class<?> impl);
    boolean isRunning();
    int getPort();
}
