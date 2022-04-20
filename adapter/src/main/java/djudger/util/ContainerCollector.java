package djudger.util;

import djudger.Allocator;
import djudger.Container;
import djudger.LangEnum;
import djudger.entity.Lang;
import org.apache.logging.log4j.Level;

import java.util.Map;

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
            PropertyUtil.logger.log(Level.WARN,"[ContGC]Detecting useless containers");
            for(LangEnum langEnum: LangEnum.values()){
                Lang lang = Allocator.lang.get(langEnum);
                if(lang == null) continue;
                Map<String, Container> containers = lang.getContainers();
                if(lang.getTaskQueue().size() == 0){
                    for (Container x:containers.values()) {
                        PropertyUtil.logger.log(Level.WARN,"[ContGC]Try to remove container " + x.getCid());
                        x.setStopped(true);
                        x.interrupt();
                        break;
                    }
                }
            }
        }
    }
}
