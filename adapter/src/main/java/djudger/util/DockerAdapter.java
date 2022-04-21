package djudger.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import djudger.Container;
import djudger.StatusEnum;
import djudger.entity.DockerException;
import djudger.entity.Lang;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DockerAdapter {

    public static DockerClient dockerClient;

    public static void init() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(PropertyUtil.dockerSocket).build();
        dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public static String createContainer(Lang language) {
        HostConfig hostConfig;
        if(PropertyUtil.seccomp){
            List<String> securityOpt = new ArrayList<>();
            securityOpt.add("seccomp=" + PropertyUtil.seccompFile);
            hostConfig = HostConfig.newHostConfig().withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none").withSecurityOpts(securityOpt);
        }
        else hostConfig = HostConfig.newHostConfig().withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none");
        CreateContainerResponse response = dockerClient.createContainerCmd(language.getImageName()).withTty(true).withHostConfig(hostConfig).withWorkingDir("/code").exec();
        dockerClient.startContainerCmd(response.getId()).exec();
        PropertyUtil.logger.log(Level.INFO, "[Docker]Container " + response.getId() + " for " + language.getType().getFileSymbol() + " created");
        return response.getId();
    }

    public static void removeContainer(Container container) {
        dockerClient.killContainerCmd(container.getCid()).exec();
        PropertyUtil.logger.log(Level.INFO, "[Docker]Container " + container.getCid() + " removed");
    }

    public static String[] runCommand(String id, String command) throws DockerException {
        OutputStream stdout = new ByteArrayOutputStream();
        OutputStream stderr = new ByteArrayOutputStream();
        PropertyUtil.logger.log(Level.INFO, "[Docker]Run command " + command + " in " + id);
        boolean completion;
        try {
            completion = dockerClient
                    .execStartCmd(dockerClient.execCreateCmd(id).withAttachStdout(true).withAttachStderr(true).withCmd(new String[]{"/bin/bash", "-c", command}).exec().getId())
                    .exec(new ExecStartResultCallback(stdout, stderr))
                    .awaitCompletion(PropertyUtil.timeLimit, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            PropertyUtil.logger.log(Level.ERROR, "[Docker]Run Error in " + id);
            throw new DockerException(StatusEnum.ERROR);
        }
        if(!completion){
            PropertyUtil.logger.log(Level.WARN, "[Docker]Timeout in " + id);
            throw new DockerException(StatusEnum.TIMEOUT);
        }
        return new String[]{stdout.toString(),stderr.toString()};
    }

    public static void copyFile(Container container, String hostPath, String remotePath){
        dockerClient.copyArchiveToContainerCmd(container.getCid()).withHostResource(hostPath).withRemotePath(remotePath).exec();
    }
}
