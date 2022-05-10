package djudger;

public class DJudgerException extends Exception {
    public StatusEnum statusEnum;

    public DJudgerException(StatusEnum statusEnum) {
        super(statusEnum.getDescribe());
        this.statusEnum = statusEnum;
    }

    public DJudgerException(String describe) {
        super(describe);
    }
}
