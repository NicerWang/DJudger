package judger;

import java.util.HashMap;
import java.util.Map;

public class Allocator {
    static Map<LangEnum, Lang> lang = new HashMap<>();

    static class Producer extends Thread{
        Lang target;
        String task;

        @Override
        public void run() {
            super.run();
            synchronized (target.getType()){
                if(target.taskQueue.size() == 0){
                    for(int i = 0; i < target.containers.size() - 1; i++){
                        target.containers.get(i).setStopped(true);
                    }
                }
                target.getTaskQueue().add(task);
                if(target.containers.size() == 0 || target.containers.size() * PropertyUtil.queuedTaskCnt < target.taskQueue.size() && target.containers.size() < PropertyUtil.maxContainer){
                    addContainer();
                }
            }
        }

        public Producer(Lang target, String task) {
            this.target = target;
            this.task = task;
        }

        private void addContainer(){
            Container container = new Container(DockerAdapter.createContainer(target),target);
            target.containers.add(container);
            container.start();
        }

    }

    public static void runCode(LangEnum langEnum, String task){
        Producer producer = new Producer(lang.get(langEnum),task);
        producer.start();
    }

    public static void init(){
        PropertyUtil.init();
        DockerAdapter.init();
    }
}
