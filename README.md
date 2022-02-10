# Judger

Docker container as sandBox for running codes.

Waiting for implementation.

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

   Use seccomp security profile `seccomp/v1.json`, add options when run:

   ```bash
   --security-opt seccomp=/path/to/files/seccomp/v1.json
   ```

   Docker provides with [default.json](https://github.com/moby/moby/blob/master/profiles/seccomp/default.json) as sample.

3. Restore

   Run testcode after each run, remove and run a new container if test failed.

4. Time Limit

   Remove and run a new container if TLE (Run or Compile) .

5. Net Work

   Container should not be linked with any network, add options when run:

   ```bash
   --network none
   ```
   
6. CPU&Process

   ```bash
   --cpus=1
   --pids-limit 30
   ```

## Other Features

1. Each language has a specific image, can have any number of containers.
2. If container wasn't broken(test passed), it will be reused.

## Possible Solution

* https://stackoverflow.com/questions/58592586/how-to-solve-permission-denied-when-mounting-volume-during-docker-run-command/58604483#58604483

* ```bash
  sysctl -w user.max_user_namespaces=15000
  ```

  

