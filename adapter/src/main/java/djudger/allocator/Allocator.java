package djudger.allocator;

import djudger.*;
import djudger.util.DockerAdapter;
import djudger.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class Allocator {
    public abstract static class AllocatorConfig {}

    protected final DockerAdapter dockerAdapter;
    public boolean stopped;
    protected final Map<String, LangConfig> langConfigMap = new HashMap<>();
    protected final Config config;


    public Allocator(Config config) {
        dockerAdapter = new DockerAdapter(config);
        this.config = config;
        for (LangConfig langConfig : config.languageConfig) {
            langConfigMap.put(langConfig.languageName, langConfig);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                super.run();
                release();
            }
        });
    }

    protected abstract void runCodeCore(LangConfig target, Task task);

    protected abstract void removeAllContainers();

    public final Task runCode(String language, List<String> commands, Integer timeLimit, TimeUnit timeUnit, String codeIdentifier, String code) throws Throwable {
        if (stopped) throw new DJudgerException("DJudger has stopped");

        LangConfig target = langConfigMap.get(language);
        if (target == null) throw new DJudgerException("Language not supported");
        Task task = preProcess(language, langConfigMap.get(language).languageFileName, commands, codeIdentifier, code, target);
        task.setTimeUnit(timeUnit);
        task.setTimeLimit(timeLimit);

        runCodeCore(target, task);

        synchronized (task) {
            while (task.getStatus() == StatusEnum.PENDING) {
                try {
                    task.wait(task.getTimeUnit().toMillis(task.getTimeLimit()) * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return task;
    }

    protected Task preProcess(String language, String fileName, List<String> rawCommands, String codeIdentifier, String code, LangConfig target) throws Throwable {
        if (language == null || rawCommands == null || codeIdentifier == null || code == null || rawCommands.size() == 0 || codeIdentifier.length() == 0 || code.length() == 0) {
            throw new IllegalArgumentException("No Args should be null");
        }
        Task task = new Task(code, codeIdentifier);

        ArrayList<String> commands = new ArrayList<>(rawCommands);
        task.setCommands(commands);
        String affix = "/" + language + "/" + codeIdentifier;
        fileName = "/" + fileName;
        String hostFilePath = config.codePath + affix + fileName;
        String remoteFilePath = "/code" + affix + fileName;

        try {
            FileUtil.writeCode(config.codePath + affix, hostFilePath, code);
        } catch (IOException e) {
            throw new DJudgerException("IO Error").initCause(e);
        }
        task.setRemotePath("/code/" + language);
        task.setHostPath(config.codePath + affix);

        for (int i = 0; i < task.getCommands().size(); i++) {
            commands.set(i, commands.get(i).replace("$(directory)", "/code" + affix));
            commands.set(i, commands.get(i).replace("$(code)", remoteFilePath));
        }
        return task;
    }

    public void release() {
        stopped = true;
        removeAllContainers();
    }
}
