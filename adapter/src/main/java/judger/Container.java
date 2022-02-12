package judger;

import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class Container extends Thread{
    private String cid;
    private Lang lang;
    private Boolean isStopped;
    private List<String> results;

    @Override
    public void run() {
        super.run();
        String task;
        while(true){
            while (lang.taskQueue.isEmpty()){
                try {
                    lang.wait();
                    if(isStopped){
                        remove();
                        PropertyUtil.logger.log(Level.ERROR,"[CONT]Test for " + cid + " failed, will be removed.");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lang.getType()){
                task = lang.taskQueue.peek();
            }
            try {
                 results.add(DockerAdapter.runCommand(cid,task));
                 if(!test()){
                     remove();
                     PropertyUtil.logger.log(Level.ERROR,"[CONT]Test for " + cid + " failed, will be removed.");
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

    private void remove(){
        synchronized (lang.getType()){
            for(int i = 0; i < lang.containers.size(); i++){
                if(lang.containers.get(i).getCid().equals(cid)){
                    lang.containers.remove(i);
                    break;
                }
            }
        }
        DockerAdapter.removeContainer(this);
    }

    public Container(String cid, Lang lang) {
        this.cid = cid;
        this.lang = lang;
        this.isStopped = false;
        this.results = new ArrayList<>();
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
