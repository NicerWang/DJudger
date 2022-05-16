---
title: Quick Start
sidebar: auto
---
# Quick Start

Support for three languages is provided by default: C++, Python and Java. 

After completing the following tutorials, you will be able to run code in these three languages in the project.

## Part 1 Import

0. [Optional] Follow [How To Be Safe?](how-to-be-safe.md) #1, set Docker to low permission mode.

   Must be carried out when **deployed in a production environment**, otherwise security cannot be guaranteed.

1. Build Images

   For all subfolders in containers folder，enter and run：

   ```shell
   sudo docker build -t imageName .
   ```

   **Need**： `judger_java`,`judger_c`, `judger_python` as Image Name，these are default in Config. If modified, default languages could not run.

   > Want more languages? read：[Add New Language](add-new-language.md)

2. Use Maven & Jar

   1. Move `release/adapter-1.x.x.jar` into `lib` folder of classpath.

   2. Add dependency in `pom.xml`

      ```xml
      <dependency>
        <groupId>nicer.djudger</groupId>
        <artifactId>adapter</artifactId>
        <version>1.x.x</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/adapter-1.x.x.jar</systemPath>
      </dependency>
      ```

   3. Run Sync

## Part 2 Use

* Config

  ```java
  // Build Config
  Config config = new Config(ModeEnum.CLASSIC)
    .configClassicAllocator(300,4,2); // Config Classic Allocator
    .configCodePath("/path/to/code") // Code files path in host
    .configDocker("tcp://127.0.0.1:2375") // Docker TCP/Unix Socket
    .addDefaultLang() // Add C++&Java&Python
  // Get Allocator
  Allocator allocator = AllocatorFactory.build(config);
  ```

* Run Code

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

  Result：

  ```
  Hello DJudger!
  ```

* About `commands`

  Provide two macro replacements：

  | Macro        | Replacement                                               | Sample(Default C++ Container) |
  | ------------ | --------------------------------------------------------- | ----------------------------- |
  | $(code)      | Absolute path of code files in the container              | /code/c/identifier/main.cpp   |
  | $(directory) | Absolute path of the code file directory in the container | /code/c/identifier            |

  For example, if you need to execute `C++` code：

  ```java
  List<String> cCommands = new ArrayList<>();
  cCommands.add("cd $(directory)");
  cCommands.add("g++ $(code)");
  cCommands.add("./a.out");
  ```

  
