# DJudger

Docker container as sandBox for running codes.

**Features:**

* Nearly Safe
* Easy to Use
* Rapid Response and Dispatch
* Do not prevent danger, just detect and recover

> If you want to use DJudger as **OJ code runner**, you can use `|` to deal with stdin, current version does not support to redirect stdin, I will fix it later.

## Adapter Use Guides

> I used DJudger in [nkcsavp_backend](https://github.com/nkcsavp/backend), which is a good sample of using adapter.

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

3. Add `DJudger` adapter codes in your projects, properties file explanation:

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
   | `xxx.test_command`      | See [Languages](#languages)                         |                             |
   | `xxx.test_result`       | Specify output of test pass                         | Pass                        |

4. API

   See Samples in `TestLauncher.java`

   ```java
   Allocator.init();
   String[] result = Allocator.runCode();
   /*
   	result[0] is stdout
   	result[1] is stderr
   	$(directory) stands for code directory
   	$(filename) stands for the code file
   */
   ```

5. Use Maven & Jar

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

6. Languages Support

   * C++

     Use `test.cpp` for test. Run `g++ test.cpp&&./a.out`, and `Pass` will be in stdout if passed.

   * Java

     Use `Test.java` for test. Run `javac Test.java&&java Test`, and `Pass` will be in stdout if passed.

   * Python

     Use `Test.py` for test. Run `python3 test.py`, and `Pass` will be in stdout if passed.****

## Update Log

* `#include</dev/random>` in `v0.3.0`

  Compiler will stop itself, you can manage that by customize commands.

## Security Features

1. Map root in container to a user without privilege.

   > Ref:https://docs.docker.com/engine/security/userns-remap/

   Add to `/etc/docker/daemon.json`,and restart docker service.

   ```json
   "userns-remap": "default"
   ```

   Then `grep dockremap /etc/subuid`,you will get at least a line:

   ```
   dockremap:100000:65535
   ```

   > **Attention**
   >
   > All images and containers may be removed, backup if necessary.

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

> coresponding docker command:
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
