---
title: 快速入门
sidebar: auto
---
# 快速入门

默认提供了三种语言的支持：C++、Python、Java，完成以下教程，您将可以在项目中（编译）运行这三种语言的代码。

## Part 1 配置

0. [可选] 根据[安全性是如何实现的？](how-to-be-safe.md)中的 #1 将 Docker 设置为低权限模式。

   在**生产环境**下部署，**必须**进行这一配置，否则无法保证安全性。

1. 构建镜像

   对于 containers 文件夹下的所有子文件夹，进入后执行：

   ```shell
   sudo docker build -t imageName .
   ```

   **要求使用**： `judger_java`，`judger_c`， `judger_python`这三个固定的 imageName ，这些是 Config 中的默认值。如果修改这些名称，会导致默认提供的语言无法使用。

   > 如果需要添加新的语言，参阅：[添加一门新的编程语言](add-new-language.md)

2. 使用 Maven 和 Jar 包导入项目

   1. 将 `release/adapter-1.x.x.jar` 移动到项目根目录下的某个目录(推荐 lib)

   2. 在 `pom.xml`中添加依赖：

      ```xml
      <dependency>
        <groupId>nicer.djudger</groupId>
        <artifactId>adapter</artifactId>
        <version>1.x.x</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/adapter-1.x.x.jar</systemPath>
      </dependency>
      ```

   3. 执行 Sync

## Part 2 使用

* 配置

  ```java
  // 构建配置
  Config config = new Config(ModeEnum.CLASSIC)
    .configClassicAllocator(300,4,2); // 配置Classic Allocator
    .configCodePath("/path/to/code") // 代码存放目录
    .configDocker("tcp://127.0.0.1:2375") // Docker TCP/Unix Socket
    .addDefaultLang() // 添加C++/Java/Python三种语言
  // 获取Allocator
  Allocator allocator = AllocatorFactory.build(config);
  ```

* 执行代码

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
  
  上述代码得到的结果为：
  
  ```
  Hello DJudger!
  ```
  
* 关于`commands`

  提供两个宏替换：

  | 宏           | 替换内容                   | 示例（以默认的C++容器为例） |
  | ------------ | -------------------------- | --------------------------- |
  | $(code)      | 容器内代码文件绝对路径     | /code/c/identifier/main.cpp |
  | $(directory) | 容器内代码文件目录绝对路径 | /code/c/identifier          |

  例如，如果需要执行`C++`代码，则：

  ```java
  List<String> cCommands = new ArrayList<>();
  cCommands.add("cd $(directory)");
  cCommands.add("g++ $(code)");
  cCommands.add("./a.out");
  ```

  

