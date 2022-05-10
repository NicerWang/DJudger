package djudger.allocator.classic;

import djudger.util.LogUtil;

import java.util.Map;

public class ContainerCollector extends Thread {
    final Integer collectInterval;
    final Map<String, Lang> langMap;

    public ContainerCollector(Integer collectInterval, Map<String, Lang> langMap) {
        this.collectInterval = collectInterval;
        this.langMap = langMap;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                sleep(collectInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtil.logger.warn("[CONT.GC]Detecting Useless Containers");
            for (Lang lang : langMap.values()) {
                Map<String, ClassicContainerThread> containers = lang.getContainers();
                if (lang.getTaskQueue().size() == 0 && containers.size() > 1) {
                    for (ClassicContainerThread x : containers.values()) {
                        LogUtil.logger.warn("[CONT.GC]Try To Remove Container {}", x.getContainer().getCid().substring(0, 8));
                        x.getContainer().setStopped(true);
                        x.interrupt();
                        break;
                    }
                }
            }
        }
    }
}
