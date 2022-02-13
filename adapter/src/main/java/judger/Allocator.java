package judger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Allocator {
    static Map<LangEnum, Lang> lang = new HashMap<>();

    public static String runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code){
        Task task = new Task(code, codeIdentifier);
        task.commands = new ArrayList<>(commands);
        Lang target = lang.get(langEnum);
        String[] paths = FileUtil.writeCode(target.getType(),task.getCodeIdentifier(),task.getCode());
        for(int i = 0; i < task.commands.size(); i++){
            task.commands.set(i,task.commands.get(i).replace("$(directory)",paths[0]));
            task.commands.set(i,task.commands.get(i).replace("$(code)",paths[1]));
        }
        synchronized (target.getType()){
            System.out.println(target.taskQueue.size());
            if(target.taskQueue.size() == 0){
                for(int i = 0; i < target.containers.size() - 1; i++){
                    target.containers.get(i).setStopped(true);
                    target.containers.get(i).interrupt();
                }
            }
            target.getTaskQueue().offer(task);
            target.taskFull.release();
            if(target.containers.size() == 0 || target.containers.size() * PropertyUtil.queuedTaskCnt < target.taskQueue.size() && target.containers.size() < PropertyUtil.maxContainer){
                addContainer(target);
            }
        }
        synchronized (task){
            try {
                task.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.getResult();
    }

    private static void addContainer(Lang target){
        Container container = new Container(DockerAdapter.createContainer(target),target);
        target.containers.add(container);
        container.start();
    }

    public static void init(){
        PropertyUtil.init();
        DockerAdapter.init();
    }
}
