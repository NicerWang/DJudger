package djudger.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import djudger.Config;
import djudger.DJudgerException;
import djudger.StatusEnum;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerAdapter {

    public final DockerClient dockerClient;
    public final Config config;

    public DockerAdapter(Config config) {
        this.config = config;
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(config.dockerSocket).build();
        dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public String createContainer(String imageName, String type) {
        HostConfig hostConfig;
        if (config.seccomp) {
            List<String> securityOpt = new ArrayList<>();
            securityOpt.add("seccomp=" + config.seccompPath);
            hostConfig = HostConfig.newHostConfig().withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none").withSecurityOpts(securityOpt);
        } else
            hostConfig = HostConfig.newHostConfig().withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none");
        CreateContainerResponse response = dockerClient.createContainerCmd(imageName).withTty(true).withHostConfig(hostConfig).withWorkingDir("/code").exec();
        dockerClient.startContainerCmd(response.getId()).exec();
        LogUtil.logger.info("[ADAPTER]Container {} for {} created", response.getId().substring(0, 8), type);
        return response.getId();
    }

    public void removeContainer(String cid) {
        dockerClient.killContainerCmd(cid).exec();
        LogUtil.logger.info("[ADAPTER]Container {} removed", cid.substring(0, 8));
    }

    public String[] runCommand(String id, String command, Integer timeLimit, TimeUnit timeUnit) throws DJudgerException {
        OutputStream stdout = new ByteArrayOutputStream();
        OutputStream stderr = new ByteArrayOutputStream();
        LogUtil.logger.info("[ADAPTER]Run command {} in {}", command, id.substring(0, 8));
        boolean completion;
        try {
            completion = dockerClient
                    .execStartCmd(dockerClient.execCreateCmd(id).withAttachStdout(true).withAttachStderr(true).withCmd(new String[]{"/bin/bash", "-c", command}).exec().getId())
                    .exec(new ExecStartResultCallback(stdout, stderr))
                    .awaitCompletion(timeLimit, timeUnit);
        } catch (InterruptedException e) {
            LogUtil.logger.error("[ADAPTER]Run Error in {}", id.substring(0, 8));
            throw new DJudgerException(StatusEnum.ERROR);
        }
        if (!completion) {
            LogUtil.logger.warn("[ADAPTER]Timeout in {}", id.substring(0, 8));
            throw new DJudgerException(StatusEnum.TIMEOUT);
        }
        return new String[]{stdout.toString(), stderr.toString()};
    }

    public void copyFile(String cid, String hostPath, String remotePath) {
        dockerClient.copyArchiveToContainerCmd(cid).withHostResource(hostPath).withRemotePath(remotePath).exec();
    }
}
