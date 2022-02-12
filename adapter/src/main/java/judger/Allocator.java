package judger;

import judger.entity.Container;
import judger.entity.Lang;
import judger.entity.LangEnum;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Allocator {
    static Map<LangEnum, Lang> lang = new HashMap<>();
    static List<Container> containers = new ArrayList<>();

    static class Producer extends Thread{
        Lang target;
        String task;

        @Override
        public void run() {
            super.run();
            synchronized (target.getType()){
                target.getTaskQueue().add(task);
            }
        }

        public Producer(Lang target, String task) {
            this.target = target;
            this.task = task;
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
