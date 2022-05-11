---
title: 常用配置项参考
sidebar: auto
---
# 常用配置项参考

## Config配置类

* 构造方法

  ```java
  Config(ModeEnum modeEnum);
  ```

  * 目前可选：（介绍见下文）

    * ModeEnum.CLASSIC - 经典模式（默认）

    * ModeEnum.THREAD_POOL - 线程池模式

* 添加配置

  * `configDocker(dockerSocket)`

    设置 Docker 的 TCP 或 Unix Socket（推荐）。

    默认为：`unix:///var/run/docker.sock`

  * `configCodePath(codePath)`

    配置代码文件存放目录。

  * `addLang()`

    参见[添加新的编程语言](add-new-language.md)。

  * `addDefaultLang()`

    添加默认的三种语言的支持，即 Java 、 Python 和 C++ 。

  * `enableSeccomp(seccompPath)`

    设置 seccomp 文件的路径，对容器进行系统调用能力限制，参见[安全性是如何保证的？](how-to-be-safe.md) #2。

## Allocator模式

### 1.ModeEnum.CLASSIC - 经典模式

DJudger v0.x版本的唯一可选模式。

每个Docker容器对应一个线程，每种语言对应一个阻塞队列，Allocator仅负责启动容器线程，并将任务放入队列，并等待任务完成。

容器线程会主动从队列中取出任务，并在对应容器进行执行。

如果容器执行出现问题，对应的线程会被结束，如果有需要，会新建一个容器线程，对应新的容器。

示例如下：

```java
Config config = new Config()
  .configCodePath("/path/to/code")
  .configDocker("unix:///var/run/docker.sock")
  .addDefaultLang()
  .configClassicAllocator(collectInterval,queuedTaskCnt,maxContainers);
```

| 名称              | 含义                                                         | 默认值 |
| ----------------- | ------------------------------------------------------------ | ------ |
| `collectInterval` | 清理无用容器的时间间隔（秒）                                 | 1800   |
| `queuedTaskCnt`   | 每个容器的最多排队任务（超出后将会增加容器，直至达到`maxContainer`） | 4      |
| `maxContainer`    | 每个语言最多的容器数目                                       | 2      |

### 2.ModeEnum.THREAD_POOL - 线程池模式

基于 Java 的 ThreadPoolExecutor ，是 DJudger v1.x 新增的 Allocator 模式，由于配置项较多，建议仅熟悉线程池的用户使用。

对于每种语言，维护一个线程池，线程池内的线程和容器一一对应，如果容器出现问题，则会删除旧容器，新建容器后替换当前线程对应的容器。

参数即为 ThreadPoolExecutor 的构造参数（除 ThreadFactory ），`BlockQueue<Runable>`和`RejectedExecutionHandler`需要使用 Lambda 表达式提供，示例如下：

```java
Config config = new Config(ModeEnum.THREAD_POOL)
  .configCodePath("/path/to/code")
  .configDocker("unix:///var/run/docker.sock")
  .addDefaultLang()
  .configThreadPoolAllocator(2,4,20,TimeUnit.SECONDS,()->new ArrayBlockingQueue<>(10),ThreadPoolExecutor.CallerRunsPolicy::new);
```

## AllocatorFactory

构造 Allocator ：

```java
Allocator allocator = AllocatorFactory.build(config);
```

## Allocator的runCode方法

```java
// 语言名称，默认Python为py、Java为java、C++为c
String lang = "py"; 
// 需要执行的命令
List<String> commands = Arrays.asList("python $(code)");
// 时间限制
Integer timeLimit = 1000;
// 时间限制的单位
TimeUnit timeUnit = TimeUnit.MILLISECONDS;
// 代码标识符
String identifier = "my_first_code";
// 代码
String code = "print(\"Hello DJudger!\")";
Task task = allocator.runCode(lang,commands,2000,timeUnit,identifier,code);
// Task对象中含有代码的运行结果等信息
System.out.print(task.getStdout());
```

## 日志

使用了Slf4j，您可以自行选择底层日志系统。
