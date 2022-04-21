package djudger;

import djudger.entity.Lang;
import djudger.entity.Task;
import djudger.util.ContainerCollector;
import djudger.util.DockerAdapter;
import djudger.util.FileUtil;
import djudger.util.PropertyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Allocator {
    public static Map<LangEnum, Lang> lang = new HashMap<>();
    static boolean stopped;

    public static Task runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code) {
        if (stopped) return null;

        Lang target = lang.get(langEnum);
        BlockingQueue<Task> taskQueue = target.getTaskQueue();
        Task task = preProcess(langEnum, commands, codeIdentifier, code, target);

        try {
            taskQueue.put(task);
            synchronized (target.getContainerCnt()){
                int size = target.getContainerCnt();
                if (size <= 0 || size * PropertyUtil.queuedTaskCnt < taskQueue.size() && size < PropertyUtil.maxContainer) {
                    addContainer(target);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (task) {
            while (task.getStatus() == StatusEnum.PENDING) {
                try {
                    task.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return task;
    }

    private static Task preProcess(LangEnum langEnum, List<String> rawCommands, String codeIdentifier, String code, Lang target) {
        if (langEnum == null || rawCommands == null || codeIdentifier == null || code == null || rawCommands.size() == 0 || codeIdentifier.length() == 0 || code.length() == 0) {
            throw new IllegalArgumentException("No Args should be null");
        }
        Task task = new Task(code, codeIdentifier);

        ArrayList<String> commands = new ArrayList<>(rawCommands);
        task.setCommands(commands);

        String[] paths = FileUtil.writeCode(target.getType(), task.getCodeIdentifier(), task.getCode());
        task.setRemotePath(paths[0].substring(0, paths[0].lastIndexOf("/") + 1));
        task.setHostPath(paths[2]);

        for (int i = 0; i < task.getCommands().size(); i++) {
            commands.set(i, commands.get(i).replace("$(directory)", paths[0]));
            commands.set(i, commands.get(i).replace("$(code)", paths[1]));
        }
        return task;
    }

    private static void addContainer(Lang target) {
        target.setContainerCnt(target.getContainerCnt() + 1);
        Container container = new Container(DockerAdapter.createContainer(target), target);
        target.getContainers().put(container.getCid(), container);
        container.start();
    }

    public static void init() {
        PropertyUtil.init();
        DockerAdapter.init();
        new ContainerCollector().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Allocator.stopped = true;
            for (LangEnum langEnum : LangEnum.values()) {
                Lang lang = Allocator.lang.get(langEnum);
                synchronized (lang.getContainerCnt()){
                    for(Container x: lang.getContainers().values()){
                        DockerAdapter.removeContainer(x);
                    }
                }
            }
        }));
    }

}
