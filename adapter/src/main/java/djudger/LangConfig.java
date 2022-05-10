package djudger;

public class LangConfig {
    public final String languageName;
    public final String languageFileName;
    public final String imageName;
    public final String testCommand;
    public final String testResult;

    public LangConfig(String languageName, String languageFileName, String imageName, String testCommand, String testResult) {
        this.languageName = languageName;
        this.languageFileName = languageFileName;
        this.imageName = imageName;
        this.testCommand = testCommand;
        this.testResult = testResult;
    }
}
