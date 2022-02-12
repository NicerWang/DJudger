package judger.entity;

public enum LangEnum {
    Java("java"),
    Python("python"),
    CPP("c");

    private final String fileSymbol;

    LangEnum(String fileSymbol) {
        this.fileSymbol = fileSymbol;
    }

    public String getFileSymbol() {
        return fileSymbol;
    }
}
