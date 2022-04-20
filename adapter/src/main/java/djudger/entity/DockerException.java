package djudger.entity;

import djudger.StatusEnum;

public class DockerException extends Exception{
    public StatusEnum statusEnum;

    public DockerException(StatusEnum statusEnum) {
        super(statusEnum.getDescribe());
        this.statusEnum = statusEnum;
    }
}
