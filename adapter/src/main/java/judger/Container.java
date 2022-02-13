package judger;

import org.apache.logging.log4j.Level;

public class Container extends Thread {
    private String cid;
    private Lang lang;
    private Boolean isStopped;

    @Override
    public void run() {
        super.run();
        Task task = null;
        while (true) {
            try {
                lang.taskFull.acquire();
                synchronized (lang.getType()) {
                    task = lang.taskQueue.poll();
                }
            } catch (InterruptedException e) {
                if (isStopped) {
                    remove();
                    PropertyUtil.logger.log(Level.ERROR, "[CONT]Container " + cid + " will be removed");
                    return;
                }
            }
            try {
                Result result = new Result();
                result.setResult(DockerAdapter.runCommand(cid, String.join("&&", task.getCommands())));
                result.setInfo(task.getCodeIdentifier());
                synchronized (lang.getResultQueue()){
                    lang.resultQueue.offer(result);
                }
                lang.getResultQueue().notifyAll();
                if (!test()) {
                    remove();
                    PropertyUtil.logger.log(Level.ERROR, "[CONT]Test for " + cid + " failed, will be removed");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean test() throws Exception {
        return DockerAdapter.runCommand(cid, lang.getTestCommand()).trim().equals(lang.getTestResult());
    }

    private void remove() {
        synchronized (lang.getType()) {
            for (int i = 0; i < lang.containers.size(); i++) {
                if (lang.containers.get(i).getCid().equals(cid)) {
                    lang.containers.remove(i);
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

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Boolean getStopped() {
        return isStopped;
    }

    public void setStopped(Boolean stopped) {
        isStopped = stopped;
    }
}
