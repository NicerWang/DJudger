package judger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lang {
    final LangEnum type;
    String imageName;
    String testCommand;
    String testResult;
    List<String> commands;
    List<Container> containers;
    Queue<String> taskQueue;

    public Lang(LangEnum type) {
        this.type = type;
        commands = new ArrayList<>();
        containers = new ArrayList<>();
        taskQueue = new LinkedList<>();
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

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
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

    public Queue<String> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(Queue<String> taskQueue) {
        this.taskQueue = taskQueue;
    }
}
