---
title: How To Be Safe?
sidebar: false
---

# How To Be Safe?

1. Map root in container to a user without privilege.

   **Attention**:All images and containers may be removed, backup if necessary.

   > Refer to:https://docs.docker.com/engine/security/userns-remap/
   >
   > If you encounter problems, refer to:https://docs.docker.com/engine/security/rootless/#troubleshooting
   >
   > If Docker also needs to run other application containers, the corresponding container needs to be executed in **host** mode.

   Add to `/etc/docker/daemon.json`, and restart docker service.

   ```json
   "userns-remap": "default"
   ```

   Then `grep dockremap /etc/subuid`, you will get at least a line, and docker containers can be start nornally:

   ```
   dockremap:100000:65535
   ```

2. Seccomp Restriction

   > Ref:https://docs.docker.com/engine/security/seccomp/

   Use seccomp security profile `seccomp/default.json`, add options when run:

   ```bash
   --security-opt seccomp=/path/to/files/seccomp/default.json
   ```

   Docker provides with [default.json](https://github.com/moby/moby/blob/master/profiles/seccomp/default.json) as sample, DJudger provides with [default.json](https://github.com/NicerWang/DJudger/blob/master/seccomp/default.json) at a smaller whitelist scope.

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
