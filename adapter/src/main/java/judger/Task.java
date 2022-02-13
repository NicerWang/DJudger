package judger;

import java.util.ArrayList;
import java.util.List;

public class Task {
    List<String> commands = new ArrayList<>();
    String code;
    String codeIdentifier;

    public Task(String code, String codeIdentifier) {
        this.code = code;
        this.codeIdentifier = codeIdentifier;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getCode() {
        return code;
    }

    public String getCodeIdentifier() {
        return codeIdentifier;
    }
}