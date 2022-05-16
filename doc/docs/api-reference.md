---
title: API Reference
sidebar: auto
---
# API Reference

## Config Class

* Constructor

  ```java
  Config(ModeEnum modeEnum);
  ```

  * Current Available：(See Allocator Mode

    * ModeEnum.CLASSIC - Classic Mode(Default)

    * ModeEnum.THREAD_POOL - Thread Pool Mode

* Add Configuration

  * `configDocker(dockerSocket)`

    Set Docker TCP or Unix Socket(Recommended).

    Default:`unix:///var/run/docker.sock`

  * `configCodePath(codePath)`

    Configure where to save code files on host.

  * `addLang()`

    Refer to [Add New Language](add-new-language.md).

  * `addDefaultLang()`

    Add default languages support: Java, Python and C++.

  * `enableSeccomp(seccompPath)`

    Set the path of the seccomp file to restrict the system call ability of the container. See [How To Be Safe?](how-to-be-safe.md) #2.

## Allocator Mode

### 1.ModeEnum.CLASSIC -  Classic Mode

The only optional mode in DJudger v0.x.

Each Docker container corresponds to a thread, and each language corresponds to a blocking queue. Allocator is only responsible for starting the container thread, putting tasks into the queue, and waiting for the task to be completed.

The container thread will actively remove tasks from the queue and execute them in the corresponding container.

If there is a problem with execution, the corresponding thread will be terminated. If necessary, a new container thread will be created corresponding to the new container.

Example:

```java
Config config = new Config()
  .configCodePath("/path/to/code")
  .configDocker("unix:///var/run/docker.sock")
  .addDefaultLang()
  .configClassicAllocator(collectInterval,queuedTaskCnt,maxContainers);
```

| Name              | Meaning                                                      | Default |
| ----------------- | ------------------------------------------------------------ | ------- |
| `collectInterval` | Time interval to remove useless containers(seconds)          | 1800    |
| `queuedTaskCnt`   | Max queued tasks for each container（Increase containers until meet `maxContainer`） | 4       |
| `maxContainer`    | Max containers for each language                             | 2       |

### 2.ModeEnum.THREAD_POOL- Thread Pool Mode

Base on ThreadPoolExecutor, is new in DJudger v1.x, which is recommended to users who are familiar with thread pool.

For each language, maintain a thread pool, and the threads in the thread pool and containers correspond one by one. If there is a problem with the container, the old container will be deleted and the container corresponding to the current thread will be replaced after creating a new container.

Parameters are the same as construction parameters of ThreadPoolExecutor (except for ThreadFactory), `BlockQueue<Runable>` and `RejectedExecutionHandler` need to be provided by Lambda expression, as follows:

```java
Config config = new Config(ModeEnum.THREAD_POOL)
  .configCodePath("/path/to/code")
  .configDocker("unix:///var/run/docker.sock")
  .addDefaultLang()
  .configThreadPoolAllocator(2,4,20,TimeUnit.SECONDS,()->new ArrayBlockingQueue<>(10),ThreadPoolExecutor.CallerRunsPolicy::new);
```

## AllocatorFactory

Build Allocator ：

```java
Allocator allocator = AllocatorFactory.build(config);
```

## Allocator runCode Method 

```java
// Language Name, default: Python as py,Java as java,C++ as c
String lang = "py"; 
// Commands to be executed
List<String> commands = Arrays.asList("python $(code)");
// Time Limit
Integer timeLimit = 1000;
// Time Limit Unit
TimeUnit timeUnit = TimeUnit.MILLISECONDS;
// Code Identifier
String identifier = "my_first_code";
// Code
String code = "print(\"Hello DJudger!\")";
Task task = allocator.runCode(lang,commands,2000,timeUnit,identifier,code);
// Task contains result
System.out.print(task.getStdout());
```

## Logging

Used Slf4j, you can choose any base log system.
