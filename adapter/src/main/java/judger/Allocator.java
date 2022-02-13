package judger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Allocator {
    static Map<LangEnum, Lang> lang = new HashMap<>();

    public static String runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code){
        Task task = new Task(code, codeIdentifier);
        task.commands = commands;
        Lang target = lang.get(langEnum);
        String filename = FileUtil.writeCode(target.getType(),task.getCodeIdentifier(),task.getCode());
        for(int i = 0; i < task.commands.size(); i++){
            task.commands.set(i,task.commands.get(i).replace("%(code)",filename));
        }
        synchronized (target.getType()){
            if(target.taskQueue.size() == 0){
                for(int i = 0; i < target.containers.size() - 1; i++){
                    target.containers.get(i).setStopped(true);
                }
            }
            target.getTaskQueue().offer(task);
            target.taskFull.release();
            if(target.containers.size() == 0 || target.containers.size() * PropertyUtil.queuedTaskCnt < target.taskQueue.size() && target.containers.size() < PropertyUtil.maxContainer){
                addContainer(target);
            }
        }
        Result result = null;
        while (true){
            synchronized (target.getResultQueue()){
                try {
                    target.getResultQueue().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(target.getResultQueue().element().info.equals(task.codeIdentifier)){
                    result = target.getResultQueue().poll();
                    break;
                }
            }
        }
        return result.getResult();
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
