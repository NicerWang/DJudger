package djudger.allocator.classic;

public class ClassicContainerThread extends Thread {
    ClassicContainer container;

    public ClassicContainerThread(ClassicContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        container.run();
    }

    public ClassicContainer getContainer() {
        return container;
    }
}
