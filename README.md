# DJudger

DJudger v1.0 docs is on the way.

Docker container as sandbox for running codes.

欢迎阅读：[中文版README](./README_zh_CN.md)

**Features:**

* Easy to Use
* Extensible
* Nearly Safe:Do not prevent danger, just detect and recover
* Rapid Response and Dispatch

> If you want to use DJudger as **OJ code runner**, you can use `|` to deal with stdin, current version does not support to redirect stdin.

## Adapter Use Guides

> I used DJudger in [nkcsavp_backend](https://github.com/nkcsavp/backend), which is a good sample of using adapter.
>
> If met some problems, you can read [Possible Problem when Starting](#Possible-Problem-when-Starting).

0. Docker

     Install Docker and see [Security Features](#Security-Features) #1 and #2.

1. Build images

   Run in folder with `Dockerfile`.

   ```bash
   sudo docker build -t judge_xxx .
   ```

   Build all three images, record all names.(recommend `judger_java`, `judger_c`, `judger_python`, which are defaults in adapter)

2. Make Folders

   Need a folder to save all code for running, the folder need three subdirectories:`c`, `py`, `java`

   If you want more languages, read [Add Other Languages](#Add-Other-Languages).

3. Use Maven & Jar

   1. Move `adapter-$(version).jar` into `lib` folder of classpath.

   2. Add dependency in `pom.xml`

      ```xml
      <dependency>
        <groupId>nicer.djudger</groupId>
        <artifactId>adapter</artifactId>
        <version>$(version)</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/adapter-$(version).jar</systemPath>
      </dependency>
      ```

   3. Run Sync

4. Add `DJudger` adapter codes in your projects, properties file explanation:

   | Name                    | Meaning                                             | Default                     |
   | ----------------------- | --------------------------------------------------- | --------------------------- |
   | `docker.socket`         | Docker TCP or Unix Socket Path                      | unix:///var/run/docker.sock |
   | `docker.code`           | Directory to save codes in `Step2`                  | /root/codes                 |
   | `docker.seccomp.enable` | Enable seccomp feature                              | true                        |
   | `docker.seccomp.path`   | Path to seccomp file                                | /root/seccomp/default.json  |
   | `time_limit`            | Max code exec time(seconds)                         | 10                          |
   | `queued_task_cnt`       | Max queued tasks each container                     | 4                           |
   | `max_container`         | Max containers for each language                    | 2                           |
   | `collect_time`          | Time interval to remove useless containers(seconds) | 1800                        |
   | `xxx.support`           | Support `xxx` language                              | true                        |
   | `xxx.image_name`        | Image for `xxx` language(Named in `Step1`)          | judger_xxx                  |
   | `xxx.test_command`      | Run test command in WORKDIR for test                |                             |
   | `xxx.test_result`       | Specify output of test pass                         | Pass                        |

5. API

   ```java
   Allocator.init();
   Task task = Allocator.runCode(LangEnum langEnum, List<String> commands, String codeIdentifier, String code);
   /*
   	1.Task(djudger.entity.Task)containes task message,result and output.
   	2.there are special meaning strings in commands：
   	  1) $(directory) stands for directory with codes
   	  2) $(filename) stands for specific code file path
   		eg. "cd $(directory)" will be replaced with "cd /path/to/directory"
     3.guarantee codeIdentifier is only, or old file would be overwrite
   */
   ```

6. Languages Support

   * C++

     Use `test.cpp` for test. Run `g++ test.cpp&&./a.out`, and `Pass` will be in stdout if passed.


   * Java

     Use `Test.java` for test. Run `javac Test.java&&java Test`, and `Pass` will be in stdout if passed.


   * Python

     Use `Test.py` for test. Run `python3 test.py`, and `Pass` will be in stdout if passed.

## Add Other Languages

1. Choose an appropriate Docker image, write test code files, and build a custom Dockerfile that contains at least the following:

   ```dockerfile
   FROM xx:version # Base Image
   
   # Follow 3 steps are necessary
   WORKDIR /code
   COPY test.xx .
   RUN mkdir suffix # eg.py/java/c
   
   CMD ["bash"]
   ```

2. Modify Adapter sources, add new languages at djudger.LangEnum, eg.`Go("go")`

3. Add corresponding configurations at adapter.properties:

   ```
   xx.support=true
   xx.image_name=judger_xx
   xx.test_command=xx test.xx
   xx.test_result=Pass
   ```

## Security Features

1. Map root in container to a user without privilege.

   > **Attention**
   >
   > All images and containers may be removed, backup if necessary.

   > Ref:https://docs.docker.com/engine/security/userns-remap/

   Add to `/etc/docker/daemon.json`,and restart docker service.

   ```json
   "userns-remap": "default"
   ```

   Then `grep dockremap /etc/subuid`,you will get at least a line:

   ```
   dockremap:100000:65535
   ```

2. Seccomp Restriction

   > Ref:https://docs.docker.com/engine/security/seccomp/

   Use seccomp security profile `seccomp/default.json`, add options when run:

   ```bash
   --security-opt seccomp=/path/to/files/seccomp/default.json
   ```

   Docker provides with [default.json](https://github.com/moby/moby/blob/master/profiles/seccomp/default.json) as sample.

3. Restore

   Run testcode after each run, remove and run a new container if test failed.

   If container wasn't broken(test passed), it will be reused.

4. Time Limit

   Remove and run a new container if TLE (Run or Compile) .

5. Network

   Container should not be linked with any network, add options when run:

   ```bash
   --network none
   ```

6. CPU&Process

   ```bash
   --cpus=1
   --pids-limit 30
   ```

> corresponding docker command:
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

## Possible Problem when Starting

* https://stackoverflow.com/questions/58592586/how-to-solve-permission-denied-when-mounting-volume-during-docker-run-command/58604483#58604483

* ```bash
  sysctl -w user.max_user_namespaces=15000
  ```

* If you need to use docker for other applications, you need open other containers as host, see docker docs of usern.
