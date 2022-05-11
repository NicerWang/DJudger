---
title: 安全性是如何保证的？
sidebar: false
---

# 安全性是如何保证的？

1. 将容器内的root用户映射到主机的低权限用户

   **注意**：所有原有的镜像和容器都会被清除，请做好备份。

   > 参考：https://docs.docker.com/engine/security/userns-remap/
   >
   > 如果遇到问题，参阅：https://docs.docker.com/engine/security/rootless/#troubleshooting
   >
   > 如果 Docker 还需要运行其他应用容器，需要以 **host** 模式执行对应容器，详见参考。

   将以下内容添加到 `/etc/docker/daemon.json`，并且重启 Docker 服务：

   ```json
   "userns-remap": "default"
   ```

   执行 `grep dockremap /etc/subuid`，其中至少有一行是这样的，且能够正常启动 Docker 容器，即为配置完成：

   ```
   dockremap:100000:65535
   ```

2. Seccomp限制

   > 参考：https://docs.docker.com/engine/security/seccomp/

   使用 Seccomp 对容器的系统调用权限进行限制，启动容器时添加：

   ```bash
   --security-opt seccomp=/path/to/files/seccomp/default.json
   ```

   Docker 官方提供了 [default.json](https://github.com/moby/moby/blob/master/profiles/seccomp/default.json) 作为默认值和示例，项目也提供了范围更小的 [default.json](https://github.com/NicerWang/DJudger/blob/master/seccomp/default.json)。

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
