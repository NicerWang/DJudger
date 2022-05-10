package djudger;

import djudger.allocator.Allocator;
import djudger.allocator.classic.ClassicAllocator;
import djudger.allocator.threadpool.ThreadPoolAllocator;

public class AllocatorFactory {
    public static Allocator build(Config config) {
        switch (config.modeEnum) {
            case CLASSIC:
                return new ClassicAllocator(config);
            case THREAD_POOL:
                return new ThreadPoolAllocator(config);
            default:
                return new ClassicAllocator(config);
        }
    }
}
