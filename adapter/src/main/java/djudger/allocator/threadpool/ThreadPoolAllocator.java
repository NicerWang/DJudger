package djudger.allocator.threadpool;

import djudger.Config;
import djudger.LangConfig;
import djudger.Task;
import djudger.allocator.Allocator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ThreadPoolAllocator extends Allocator {
    Map<String, ThreadPoolExecutor> executorMap = new HashMap<>();
    ThreadPoolAllocatorConfig threadPoolAllocatorConfig;
    Map<Long, String> containerMap = new HashMap<>();

    public ThreadPoolAllocator(Config config) {
        super(config);
        threadPoolAllocatorConfig = (ThreadPoolAllocatorConfig) config.allocatorConfig;
        for (LangConfig lang : langConfigMap.values()) {
            executorMap.put(lang.languageName, new ThreadPoolExecutor(threadPoolAllocatorConfig.corePoolSize,
                    threadPoolAllocatorConfig.maximumPoolSize, threadPoolAllocatorConfig.keepAliveTime,
                    threadPoolAllocatorConfig.timeUnit, threadPoolAllocatorConfig.workQueue.get(),
                    threadPoolAllocatorConfig.handler.get()));
        }
    }

    @Override
    protected void runCodeCore(LangConfig target, Task task) {
        ThreadPoolExecutor executor = executorMap.get(target.languageName);
        executor.execute(new ThreadPoolContainer(task, target, dockerAdapter, this));
    }

    @Override
    protected void removeAllContainers() {
        for (ThreadPoolExecutor threadPoolExecutor : executorMap.values()) {
            threadPoolExecutor.shutdownNow();
        }
        for (ThreadPoolExecutor threadPoolExecutor : executorMap.values()) {
            while (true) {
                try {
                    if (threadPoolExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (String cid : containerMap.values()) {
            dockerAdapter.removeContainer(cid);
        }
    }

    public static class ThreadPoolAllocatorConfig extends AllocatorConfig {
        public Integer corePoolSize = 4;
        public Integer maximumPoolSize = 2;
        public Integer keepAliveTime = 1800;
        public TimeUnit timeUnit;
        public Supplier<BlockingQueue<Runnable>> workQueue;
        public Supplier<RejectedExecutionHandler> handler;
    }
}
