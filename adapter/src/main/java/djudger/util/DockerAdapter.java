package djudger.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import djudger.entity.Container;
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
        Bind bind = new Bind(PropertyUtil.codePath + "/" + language.getType().getFileSymbol(), new Volume("/code/" + language.getType().getFileSymbol()));
        List<String> securityOpt = new ArrayList<>();
        securityOpt.add("seccomp=" + PropertyUtil.seccompFile);
//        HostConfig hostConfig = HostConfig.newHostConfig().withBinds(bind).withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none").withSecurityOpts(securityOpt);
        HostConfig hostConfig = HostConfig.newHostConfig().withBinds(bind).withCpuCount(1L).withPidsLimit(30L).withAutoRemove(true).withNetworkMode("none");
        CreateContainerResponse response = dockerClient.createContainerCmd(language.getImageName()).withTty(true).withHostConfig(hostConfig).withWorkingDir("/code").exec();
        dockerClient.startContainerCmd(response.getId()).exec();
        PropertyUtil.logger.log(Level.INFO, "[DOCKER]Container " + response.getId() + " for " + language.getType().getFileSymbol() + " created");
        return response.getId();
    }

    public static void removeContainer(Container container) {
        dockerClient.stopContainerCmd(container.getCid()).exec();
        PropertyUtil.logger.log(Level.INFO, "[DOCKER]Container " + container.getCid() + " removed");
    }

    public static String runCommand(String id, String command) throws Exception {
        OutputStream stdout = new ByteArrayOutputStream();
        PropertyUtil.logger.log(Level.INFO, "[DOCKER]Run command " + command + " in " + id);
        try {
            dockerClient
                    .execStartCmd(dockerClient.execCreateCmd(id).withAttachStdout(true).withAttachStderr(true).withCmd(new String[]{"/bin/bash", "-c", command}).exec().getId())
                    .exec(new ExecStartResultCallback(stdout, stdout))
                    .awaitCompletion(PropertyUtil.timeLimit, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            PropertyUtil.logger.log(Level.ERROR, "[DOCKER]Run Error in " + id);
            throw new Exception("error:" + stdout.toString(), e);
        }
        return stdout.toString();
    }
}
