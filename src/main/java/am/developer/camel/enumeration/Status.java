package am.developer.camel.enumeration;

import lombok.Getter;

@Getter
public enum Status {

    ACTIVE("00"),
    PENDING("01"),
    READY("02"),
    INACTIVE("03"),
    DISMANTLED("04"),
    STOPPED("99");

    private final String code;

    Status(String code) {
        this.code = code;
    }

}
