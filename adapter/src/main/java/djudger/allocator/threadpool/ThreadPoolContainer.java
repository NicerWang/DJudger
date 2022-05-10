package djudger.allocator.threadpool;

import djudger.LangConfig;
import djudger.Task;
import djudger.allocator.Container;
import djudger.util.DockerAdapter;


public class ThreadPoolContainer extends Container implements Runnable {
    private final Task task;
    final ThreadPoolAllocator allocator;

    private void replace() {
        if (cid != null) dockerAdapter.removeContainer(cid);
        cid = dockerAdapter.createContainer(langConfig.imageName, langConfig.languageName);
        allocator.containerMap.put(Thread.currentThread().getId(), cid);
    }

    public ThreadPoolContainer(Task task, LangConfig lang, DockerAdapter dockerAdapter, ThreadPoolAllocator allocator) {
        this.task = task;
        this.langConfig = lang;
        this.dockerAdapter = dockerAdapter;
        this.allocator = allocator;
    }

    @Override
    public void run() {
        this.cid = allocator.containerMap.get(Thread.currentThread().getId());
        if (this.cid == null) {
            replace();
        }
        executeTask(task);
    }

    @Override
    protected void exceptionHandler() {
        replace();
    }
}
