package djudger.allocator.classic;

import djudger.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Lang {
    private final Map<String, ClassicContainerThread> containers;
    private final BlockingQueue<Task> taskQueue;
    private volatile Integer containerCnt = 0;

    public Lang() {
        containers = new HashMap<>();
        taskQueue = new LinkedBlockingQueue<>();
    }

    public Map<String, ClassicContainerThread> getContainers() {
        return containers;
    }

    public BlockingQueue<Task> getTaskQueue() {
        return taskQueue;
    }

    public Integer getContainerCnt() {
        return containerCnt;
    }

    public void setContainerCnt(Integer containerCnt) {
        this.containerCnt = containerCnt;
    }
}
