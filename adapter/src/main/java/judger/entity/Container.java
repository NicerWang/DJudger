package judger.entity;

import judger.DockerAdapter;

public class Container extends Thread{
    private String cid;
    private Lang lang;

    @Override
    public void run() {
        super.run();
        synchronized (lang.getType()){
            while(!lang.taskQueue.isEmpty()){
                String task = lang.taskQueue.peek();
                try {
                    for(String command: lang.commands)
                    DockerAdapter.runCommand(cid,command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Container(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
