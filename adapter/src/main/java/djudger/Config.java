package djudger;

import djudger.allocator.Allocator;
import djudger.allocator.classic.ClassicAllocator;
import djudger.allocator.threadpool.ThreadPoolAllocator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Config {
    public ModeEnum modeEnum = ModeEnum.CLASSIC;
    public final List<LangConfig> languageConfig = new ArrayList<>();
    public String dockerSocket = "unix:///var/run/docker.sock";
    public String codePath;
    public Boolean seccomp = false;
    public String seccompPath;
    public Allocator.AllocatorConfig allocatorConfig;

    public Config() {
    }

    public Config(ModeEnum modeEnum) {
        this.modeEnum = modeEnum;
    }

    public Config addLang(String languageName, String languageFileName, String imageName, String testCommand, String testResult) {
        languageConfig.add(new LangConfig(languageName, languageFileName, imageName, testCommand, testResult));
        return this;
    }

    public Config addDefaultLang() {
        languageConfig.add(new LangConfig("c", "main.cpp", "judger_c", "g++ test.cpp&&./a.out", "Pass"));
        languageConfig.add(new LangConfig("java", "Main.java", "judger_java", "javac Test.java&&java Test", "Pass"));
        languageConfig.add(new LangConfig("py", "main.py", "judger_python", "python3 test.py", "Pass"));
        return this;
    }

    public Config enableSeccomp(String seccompPath) {
        seccomp = true;
        this.seccompPath = seccompPath;
        return this;
    }

    public Config configDocker(String dockerSocket) {
        this.dockerSocket = dockerSocket;
        return this;
    }

    public Config configCodePath(String codePath) {
        this.codePath = codePath;
        return this;
    }

    public Config configClassicAllocator(Integer collectInterval, Integer queuedTaskCnt, Integer maxContainers) {
        ClassicAllocator.ClassicAllocatorConfig classicAllocatorConfig = new ClassicAllocator.ClassicAllocatorConfig();
        classicAllocatorConfig.collectInterval = collectInterval;
        classicAllocatorConfig.queuedTaskCnt = queuedTaskCnt;
        classicAllocatorConfig.maxContainers = maxContainers;
        allocatorConfig = classicAllocatorConfig;
        return this;
    }

    public Config configThreadPoolAllocator(Integer corePoolSize, Integer maximumPoolSize, Integer keepAliveTime,
                                            TimeUnit timeUnit, Supplier<BlockingQueue<Runnable>> workQueue, Supplier<RejectedExecutionHandler> handler) {
        ThreadPoolAllocator.ThreadPoolAllocatorConfig threadPoolAllocatorConfig = new ThreadPoolAllocator.ThreadPoolAllocatorConfig();
        threadPoolAllocatorConfig.corePoolSize = corePoolSize;
        threadPoolAllocatorConfig.maximumPoolSize = maximumPoolSize;
        threadPoolAllocatorConfig.keepAliveTime = keepAliveTime;
        threadPoolAllocatorConfig.timeUnit = timeUnit;
        threadPoolAllocatorConfig.workQueue = workQueue;
        threadPoolAllocatorConfig.handler = handler;
        allocatorConfig = threadPoolAllocatorConfig;
        return this;
    }
}
