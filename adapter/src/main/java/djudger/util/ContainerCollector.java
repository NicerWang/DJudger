package djudger.util;

import djudger.Allocator;
import djudger.entity.Container;
import djudger.entity.LangEnum;
import djudger.entity.Lang;
import org.apache.logging.log4j.Level;

import java.util.List;

public class ContainerCollector extends Thread{
    @Override
    public void run() {
        super.run();
        while (true){
            try {
                sleep(PropertyUtil.collectTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PropertyUtil.logger.log(Level.WARN,"[ContCollector]Detect useless containers");
            for(LangEnum langEnum: LangEnum.values()){
                Lang lang = Allocator.lang.get(langEnum);
                if(lang == null) continue;
                List<Container> containers = lang.getContainers();
                synchronized (lang.getType()) {
                    if (lang.getTaskQueue().size() == 0) {
                        if(containers.size() == 0) continue;
                        containers.get(0).setStopped(true);
                        containers.get(0).interrupt();
                    }
                }
            }
        }
    }
}
