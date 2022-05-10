package djudger.allocator.classic;

import djudger.LangConfig;
import djudger.Task;
import djudger.allocator.Container;
import djudger.util.DockerAdapter;
import djudger.util.LogUtil;

import java.util.Map;

public class ClassicContainer extends Container implements Runnable {
    private volatile Boolean isStopped;
    final ClassicAllocator allocator;
    Lang lang;

    @Override
    public void run() {
        Task task;
        while (true) {
            try {
                task = lang.getTaskQueue().take();
            } catch (InterruptedException e) {
                if (isStopped) {
                    remove();
                    LogUtil.logger.info("[CONT]{} removed as stop signal", cid.substring(0, 8));
                } else {
                    LogUtil.logger.error("[CONT]{} removed unexpectedly", cid.substring(0, 8));
                }
                return;
            }
            boolean success = executeTask(task);
            if (!success) return;
        }
    }

    @Override
    protected void exceptionHandler() {
        remove();
    }

    private void remove() {
        synchronized (lang.getContainerCnt()) {
            lang.setContainerCnt(lang.getContainerCnt() - 1);
            Map<String, ClassicContainerThread> containers = lang.getContainers();
            containers.remove(cid);
        }
        dockerAdapter.removeContainer(cid);
        allocator.zeroContainerDetect(langConfig);
    }

    public void setStopped(Boolean stopped) {
        isStopped = stopped;
    }

    public ClassicContainer(String cid, LangConfig langConfig, DockerAdapter dockerAdapter, ClassicAllocator allocator) {
        this.cid = cid;
        this.langConfig = langConfig;
        this.dockerAdapter = dockerAdapter;
        this.isStopped = false;
        this.allocator = allocator;
        lang = allocator.langMap.get(langConfig.languageName);
    }
}
