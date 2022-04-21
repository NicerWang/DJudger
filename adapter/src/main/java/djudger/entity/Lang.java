package djudger.entity;

import djudger.Container;
import djudger.LangEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Lang {
    final LangEnum type;
    String imageName;
    String testCommand;
    String testResult;
    Map<String, Container> containers;
    BlockingQueue<Task> taskQueue;
    volatile Integer containerCnt = 0;

    public Lang(LangEnum type) {
        this.type = type;
        containers = new HashMap<>();
        taskQueue = new LinkedBlockingQueue<>();
    }

    public LangEnum getType() {
        return type;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTestCommand() {
        return testCommand;
    }

    public void setTestCommand(String testCommand) {
        this.testCommand = testCommand;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public Map<String, Container> getContainers() {
        return containers;
    }

    public void setContainers(Map<String, Container> containers) {
        this.containers = containers;
    }

    public BlockingQueue<Task> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(BlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public Integer getContainerCnt() {
        return containerCnt;
    }

    public void setContainerCnt(Integer containerCnt) {
        this.containerCnt = containerCnt;
    }
}
