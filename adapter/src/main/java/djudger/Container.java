package djudger;

import djudger.entity.DockerException;
import djudger.entity.Lang;
import djudger.entity.Task;
import djudger.util.DockerAdapter;
import djudger.util.PropertyUtil;
import org.apache.logging.log4j.Level;

import java.util.Map;

public class Container extends Thread {
    private final String cid;
    private final Lang lang;
    private volatile Boolean isStopped;

    @Override
    public void run() {
        super.run();
        Task task = null;
        while (true) {
            try {
                task = lang.getTaskQueue().take();
            } catch (InterruptedException e) {
                if (isStopped) {
                    remove();
                    PropertyUtil.logger.log(Level.INFO, "[CONT]" + cid + " removed as stop signal");
                }
                else{
                    PropertyUtil.logger.log(Level.FATAL, "[CONT]" + cid + " removed unexpectedly");
                }
                return;
            }
            PropertyUtil.logger.log(Level.INFO, "[CONT]Task " + task.getCodeIdentifier() + " run by " + cid);
            DockerAdapter.copyFile(this,task.getHostPath(),task.getRemotePath());
            String[] std;
            try {
                std = DockerAdapter.runCommand(cid, String.join("&&", task.getCommands()));
            } catch (DockerException e) {
                synchronized (task) {
                    task.setStatus(e.statusEnum);
                    task.notifyAll();
                }
                remove();
                PropertyUtil.logger.log(Level.ERROR, "[CONT]" + cid + " removed as run error");
                return;
            }
            synchronized (task) {
                task.setStdout(std[0]);
                task.setStderr(std[1]);
                task.setStatus(StatusEnum.OK);
                task.notifyAll();
            }
            try {
                if (!test()) {
                    remove();
                    PropertyUtil.logger.log(Level.ERROR, "[CONT]" + cid + " removed as run error");
                    return;
                }
            } catch (Exception e) {
                remove();
                PropertyUtil.logger.log(Level.ERROR, "[CONT]" + cid + " removed as run error");
                return;
            }
        }
    }

    private boolean test() throws Exception {
        return DockerAdapter.runCommand(cid, lang.getTestCommand())[0].trim().equals(lang.getTestResult());
    }

    private void remove() {
        synchronized (lang.getContainerCnt()){
            lang.setContainerCnt(lang.getContainerCnt() - 1);
            Map<String, Container> containers = lang.getContainers();
            containers.remove(cid);
        }
        DockerAdapter.removeContainer(this);
    }

    public Container(String cid, Lang lang) {
        this.cid = cid;
        this.lang = lang;
        this.isStopped = false;
    }

    public String getCid() {
        return cid;
    }

    public void setStopped(Boolean stopped) {
        isStopped = stopped;
    }
}
