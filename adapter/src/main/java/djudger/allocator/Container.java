package djudger.allocator;

import djudger.DJudgerException;
import djudger.LangConfig;
import djudger.StatusEnum;
import djudger.Task;
import djudger.util.DockerAdapter;
import djudger.util.LogUtil;

import java.util.concurrent.TimeUnit;

public abstract class Container {
    protected String cid;
    protected DockerAdapter dockerAdapter;
    protected LangConfig langConfig;

    public String getCid() {
        return cid;
    }

    protected boolean executeTask(Task task) {
        LogUtil.logger.info("[CONT]Task {} run by {}", task.getCodeIdentifier(), cid.substring(0, 8));
        dockerAdapter.copyFile(cid, task.getHostPath(), task.getRemotePath());
        String[] std;
        try {
            std = dockerAdapter.runCommand(cid, String.join("&&", task.getCommands()), task.getTimeLimit(), task.getTimeUnit());
        } catch (DJudgerException e) {
            synchronized (task) {
                task.setStatus(e.statusEnum);
                task.notifyAll();
            }
            exceptionHandler();
            LogUtil.logger.error("[CONT]{} removed as run error", cid);
            return false;
        }
        synchronized (task) {
            task.setStdout(std[0]);
            task.setStderr(std[1]);
            task.setStatus(StatusEnum.OK);
            task.notifyAll();
        }
        try {
            if (!test()) {
                exceptionHandler();
                LogUtil.logger.error("[CONT]{} removed as run error", cid);
                return false;
            }
        } catch (Exception e) {
            exceptionHandler();
            LogUtil.logger.error("[CONT]{} removed as run error", cid);
            return false;
        }
        return true;
    }

    private boolean test() throws Exception {
        return dockerAdapter.runCommand(cid, langConfig.testCommand, 2000, TimeUnit.MILLISECONDS)[0].trim().equals(langConfig.testResult);
    }

    protected abstract void exceptionHandler();
}
