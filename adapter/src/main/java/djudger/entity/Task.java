package djudger.entity;

import djudger.StatusEnum;

import java.util.ArrayList;
import java.util.List;

public class Task {
    List<String> commands = new ArrayList<>();
    String code;
    String codeIdentifier;
    String stdout;
    String stderr;
    String hostPath;
    String remotePath;
    StatusEnum status;

    public Task(String code, String codeIdentifier) {
        this.code = code;
        this.codeIdentifier = codeIdentifier;
        this.status = StatusEnum.PENDING;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeIdentifier() {
        return codeIdentifier;
    }

    public void setCodeIdentifier(String codeIdentifier) {
        this.codeIdentifier = codeIdentifier;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public String getHostPath() {
        return hostPath;
    }

    public void setHostPath(String hostPath) {
        this.hostPath = hostPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "commands=" + commands +
                ", code='" + code + '\'' +
                ", codeIdentifier='" + codeIdentifier + '\'' +
                ", stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                ", hostPath='" + hostPath + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", status=" + status +
                '}';
    }
}