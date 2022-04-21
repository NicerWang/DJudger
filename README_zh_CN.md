# DJudger

基于 Docker 的代码执行容器。

**特性：**

* 使用简单
* 可拓展性强

* 几乎安全，不预防风险，而是探测并恢复
* 快速响应和分配

> 如果你想使用 DJudger 作为 OJ 的代码运行器，你可以尝试使用管道符 `|` 来解决输入问题，目前版本暂时不支持重定向 stdin 。

## Adapter用户指南

> 我在[nkcsavp_backend](https://github.com/nkcsavp/backend)使用了 DJudger ，这是一个使用 Adapter 的示例。
>
> 如果配置过程中遇到问题，可以参阅[可能遇到的问题](#可能遇到的问题)。

0. Docker

      在服务器安装Docker，并参照[安全特性](#安全特性) #1 和 #2 进行配置。

1. 构建镜像

   在`Dockerfile`所在目录下执行：

   ```bash
   sudo docker build -t judge_xxx .
   ```

   构建所有的镜像，记录所有镜像的名称（推荐使用 `judger_java`，`judger_c`， `judger_python`，这些是 Adapter 中的默认值）

2. 创建对应文件夹

   需要创建一个存放代码的文件夹，这个文件夹应该包含多个文件夹，以存储不同语言的代码，默认需要三个：`c`， `py`， `java`。

   如果需要添加其他语言支持，请参阅[添加其他语言](#添加其他语言)。

3. 使用 Maven 和 Jar 包导入项目

   1. 将 `adapter-$(version).jar` 移动到项目classpath的某个目录(如 lib)

   2. 在 `pom.xml`中添加依赖：

      ```xml
      <dependency>
        <groupId>nicer.djudger</groupId>
        <artifactId>adapter</artifactId>
        <version>$(version)</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/adapter-$(version).jar</systemPath>
      </dependency>
      ```

   3. 执行Sync

4. 需要准备配置文件 `adapter.properties` ，配置文件解释如下：

   | 名称                    | 含义                                                         | 缺省值                      |
   | ----------------------- | ------------------------------------------------------------ | --------------------------- |
   | `docker.socket`         | Docker TCP 或 Unix Socket 路径                               | unix:///var/run/docker.sock |
   | `docker.code`           | #2中提到的存放代码的文件夹                                   | /root/codes                 |
   | `docker.seccomp.enable` | 打开seccomp限制                                              | true                        |
   | `docker.seccomp.path`   | seccomp文件路径                                              | /root/seccomp/default.json  |
   | `time_limit`            | 最长代码执行时间（秒）                                       | 10                          |
   | `queued_task_cnt`       | 每个容器的最多排队任务（超出后将会增加容器，直至达到`max_container`） | 4                           |
   | `max_container`         | 每个语言最多的容器数目                                       | 2                           |
   | `collect_time`          | 清理无用容器的时间间隔（秒）                                 | 1800                        |
   | `xx.support`            | `xx`语言支持                                                 | true                        |
   | `xx.image_name`         | `xx`语言对应的镜像名，在#1中进行了设置                       | judger_xxx                  |
   | `xx.test_command`       | 在容器的WORKDIR执行对应命令进行测试，详见语言支持部分        |                             |
   | `xx.test_result`        | 指明测试成功时的 stdout 结果                                 | Pass                        |

5. Adapter具体使用流程

   ```java
   Allocator.init();
   Task task = Allocator.runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code);
   /*
   	1.Task对象(djudger.entity.Task)包含任务信息、运行结果、输出结果等
   	2.在commands中有以下可替换内容：
   	  1) $(directory) 代表代码存放的目录
   	  2) $(filename) 代表代码文件名
   		例如 "cd $(directory)" 在执行时将会被替换为 "cd 代码文件在Docker容器内的具体目录"
     3.保证codeIdentifier不会产生重复，否则旧代码文件会被覆盖
   */
   ```

6. 语言支持

   * C++

     使用 `test.cpp` 进行测试. 执行 `g++ test.cpp&&./a.out`进行测试，如果测试通过，stdout 将只包含字符串 Pass。

   * Java

     使用 `Test.java` 进行测试. 执行 `javac Test.java&&java Test`，如果测试通过，stdout 将只包含字符串 Pass。

   * Python

     使用 `Test.py` 进行测试. 执行 `python3 test.py`，如果测试通过，stdout 将只包含字符串 Pass。

## 添加其他语言

1. 选择合适的Docker镜像，编写测试代码文件，并定制Dockerfile进行构建，其中至少包含以下内容：

   ```dockerfile
   FROM xx:version # 基础镜像
   
   # 以下三步是必须的
   WORKDIR /code
   COPY test.xx .
   RUN mkdir 语言文件后缀 # 如py/java/c
   
   CMD ["bash"]
   ```

2. 改动 Adapter 源代码，在 djudger.LangEnum 添加新的语言，如`Go("go")`

3. 在 adapter.properties 中添加对应的配置：

   ```
   xx.support=true
   xx.image_name=judger_xx
   xx.test_command=xx test.xx
   xx.test_result=Pass
   ```

## 安全特性

1. 将容器内的root用户映射到主机的低权限用户

   > **注意**
   >
   > 所有原有的镜像和容器都会被清除，请做好备份。

   > 参考：https://docs.docker.com/engine/security/userns-remap/

   将以下内容添加到 `/etc/docker/daemon.json`，并且重启 Docker 服务：

   ```json
   "userns-remap": "default"
   ```

   执行 `grep dockremap /etc/subuid`，其中至少有一行是这样的：

   ```
   dockremap:100000:65535
   ```

2. Seccomp限制

   > 参考：https://docs.docker.com/engine/security/seccomp/

   使用Seccomp对容器的系统调用权限进行限制，启动容器时添加：

   ```bash
   --security-opt seccomp=/path/to/files/seccomp/default.json
   ```

   Docker官方提供了 [default.json](https://github.com/moby/moby/blob/master/profiles/seccomp/default.json) 作为默认值和示例。

3. 恢复策略

   在每次代码运行结束后，执行测试代码，如果测试失败，则认为容器被破坏，删除容器；

   如果测试通过，则认为容器能够正常运行，当前容器将会被复用。

4. 时间限制

   如果执行超时，则认为容器被破坏，删除容器。

5. 网络

   容器不能和任何网络连接，启动容器时添加：

   ```bash
   --network none
   ```

6. CPU和进程数限制

   ```bash
   --cpus=1
   --pids-limit 30
   ```

> 对应的Docker命令合集：
>
> ```shell
> sudo docker run -it \
>     --name judger-xxx \
>     -v /path/to/code/xxx:/code/xxx \
>     --security-opt seccomp=/path/to/seccomp/default.json \
>     --network none \
>     --cpus=1 \
>     --pids-limit 30 \
>     judger-xxx
> ```

## 可能遇到的问题

* https://stackoverflow.com/questions/58592586/how-to-solve-permission-denied-when-mounting-volume-during-docker-run-command/58604483#58604483

* ```bash
  sysctl -w user.max_user_namespaces=15000
  ```

* 如果Docker还需要运行其他应用容器，需要以host模式执行对应容器，详见关于usern的Docker文档
