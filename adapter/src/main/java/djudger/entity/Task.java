package djudger.entity;

import java.util.ArrayList;
import java.util.List;

public class Task {
    List<String> commands = new ArrayList<>();
    String code;
    String codeIdentifier;
    String result;

    public Task(String code, String codeIdentifier) {
        this.code = code;
        this.codeIdentifier = codeIdentifier;
        this.result = null;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}