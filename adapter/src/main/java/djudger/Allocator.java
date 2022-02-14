package djudger;

import djudger.entity.Container;
import djudger.entity.Lang;
import djudger.entity.LangEnum;
import djudger.entity.Task;
import djudger.util.ContainerCollector;
import djudger.util.DockerAdapter;
import djudger.util.FileUtil;
import djudger.util.PropertyUtil;

import java.util.*;

public class Allocator {
    public static Map<LangEnum, Lang> lang = new HashMap<>();
    static boolean stopped;

    public static String runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code) {
        if (stopped) return null;
        Task task = new Task(code, codeIdentifier);
        commands = new ArrayList<>(commands);
        task.setCommands(commands);
        Lang target = lang.get(langEnum);
        List<Container> containers = target.getContainers();
        Queue<Task> taskQueue = target.getTaskQueue();
        String[] paths = FileUtil.writeCode(target.getType(), task.getCodeIdentifier(), task.getCode());
        task.setRemotePath(paths[0].substring(0,paths[0].lastIndexOf("/") + 1));
        task.setHostPath(paths[2]);
        for (int i = 0; i < task.getCommands().size(); i++) {
            commands.set(i, commands.get(i).replace("$(directory)", paths[0]));
            commands.set(i, commands.get(i).replace("$(code)", paths[1]));
        }
        synchronized (target.getType()) {
            taskQueue.offer(task);
            target.getTaskFull().release();
            if (containers.size() == 0 || containers.size() * PropertyUtil.queuedTaskCnt < taskQueue.size() && containers.size() < PropertyUtil.maxContainer) {
                addContainer(target);
            }
        }
        synchronized (task) {
            try {
                task.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.getResult();
    }

    private static void addContainer(Lang target) {
        Container container = new Container(DockerAdapter.createContainer(target), target);
        target.getContainers().add(container);
        container.start();
    }

    public static void init() {
        PropertyUtil.init();
        DockerAdapter.init();
        new ContainerCollector().start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Allocator.stopped = true;
                for (LangEnum langEnum : LangEnum.values()) {
                    Lang lang = Allocator.lang.get(langEnum);
                    List<Container> containers = lang.getContainers();
                    synchronized (lang.getType()) {
                        for (int i = 0; i < containers.size(); i++) {
                            DockerAdapter.removeContainer(containers.get(i));
                        }
                    }
                }
            }
        });
    }

}
