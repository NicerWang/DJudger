package djudger.allocator.classic;

import djudger.Config;
import djudger.LangConfig;
import djudger.Task;
import djudger.allocator.Allocator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ClassicAllocator extends Allocator {

    final ClassicAllocatorConfig classicAllocatorConfig;
    Map<String, Lang> langMap = new HashMap<>();

    public ClassicAllocator(Config config) {
        super(config);
        classicAllocatorConfig = (ClassicAllocatorConfig) config.allocatorConfig;
        for (LangConfig langConfig : langConfigMap.values()) {
            langMap.put(langConfig.languageName, new Lang());
        }
        new ContainerCollector(classicAllocatorConfig.collectInterval, langMap).start();
    }

    @Override
    protected void runCodeCore(LangConfig target, Task task) {
        Lang lang = langMap.get(target.languageName);
        BlockingQueue<Task> taskQueue = lang.getTaskQueue();
        try {
            taskQueue.put(task);
            synchronized (lang.getContainerCnt()) {
                int size = lang.getContainerCnt();
                if (size <= 0 || size * classicAllocatorConfig.queuedTaskCnt < taskQueue.size() && size < classicAllocatorConfig.maxContainers) {
                    addContainer(target);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void removeAllContainers() {
        for (Lang lang : langMap.values()) {
            synchronized (lang.getContainerCnt()) {
                for (ClassicContainerThread x : lang.getContainers().values()) {
                    dockerAdapter.removeContainer(x.getContainer().getCid());
                }
            }
        }
    }

    public static class ClassicAllocatorConfig extends AllocatorConfig {
        public Integer queuedTaskCnt = 4;
        public Integer maxContainers = 2;
        public Integer collectInterval = 1800;
    }

    public void zeroContainerDetect(LangConfig langConfig) {
        Lang lang = langMap.get(langConfig.languageName);
        synchronized (lang.getContainerCnt()) {
            int size = lang.getContainerCnt();
            if (size <= 0 || size * classicAllocatorConfig.queuedTaskCnt < lang.getTaskQueue().size() && size < classicAllocatorConfig.maxContainers) {
                addContainer(langConfig);
            }
        }
    }

    private void addContainer(LangConfig langConfig) {
        Lang lang = langMap.get(langConfig.languageName);
        lang.setContainerCnt(lang.getContainerCnt() + 1);
        ClassicContainer container = new ClassicContainer(dockerAdapter.createContainer(langConfig.imageName, langConfig.languageName), langConfig, dockerAdapter, this);
        ClassicContainerThread thread = new ClassicContainerThread(container);
        lang.getContainers().put(container.getCid(), thread);
        thread.start();
    }

}
