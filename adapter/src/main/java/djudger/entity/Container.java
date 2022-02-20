package djudger.entity;

import djudger.util.DockerAdapter;
import djudger.util.PropertyUtil;
import org.apache.logging.log4j.Level;

import java.util.List;

public class Container extends Thread {
    private final String cid;
    private final Lang lang;
    private Boolean isStopped;

    @Override
    public void run() {
        super.run();
        Task task = null;
        while (true) {
            try {
                lang.getTaskFull().acquire();
                synchronized (lang.getType()) {
                    task = lang.getTaskQueue().poll();
                }
                PropertyUtil.logger.log(Level.INFO, "[CONT]Task " + task.getCodeIdentifier() + " run by " + cid);
            } catch (InterruptedException e) {
                if (isStopped) {
                    remove();
                    PropertyUtil.logger.log(Level.INFO, "[CONT]" + cid + " removed as stop signal");
                    return;
                }
            }
            DockerAdapter.copyFile(this,task.getHostPath(),task.getRemotePath());
            String[] std;
            try {
                std = DockerAdapter.runCommand(cid, String.join("&&", task.getCommands()));
            } catch (Exception e) {
                synchronized (task) {
                    task.notify();
                }
                remove();
                PropertyUtil.logger.log(Level.ERROR, "[CONT]" + cid + " removed as run error");
                return;
            }
            synchronized (task) {
                task.setStdout(std[0]);
                task.setStderr(std[1]);
                task.notify();
            }
            try {
                if (!test()) {
                    remove();
                    PropertyUtil.logger.log(Level.ERROR, "[CONT]" + cid + " removed as run error");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean test() throws Exception {
        return DockerAdapter.runCommand(cid, lang.getTestCommand())[0].trim().equals(lang.getTestResult());
    }

    private void remove() {
        synchronized (lang.getType()) {
            List<Container> containers = lang.getContainers();
            for (int i = 0; i < containers.size(); i++) {
                if (containers.get(i).getCid().equals(cid)) {
                    containers.remove(i);
                    break;
                }
            }
        }
        DockerAdapter.removeContainer(this);
        this.interrupt();
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
