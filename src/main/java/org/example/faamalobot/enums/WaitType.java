package org.example.faamalobot.enums;

public enum WaitType {
    WAIT_FOR_NEW_YEAR_BOY_NAME,
    WAIT_FOR_NEW_YEAR_GIRL_NAME,
    WAIT_FOR_JUMA_GIRL_NAME,
    WAIT_FOR_JUMA_BOY_NAME,
    WAIT_FOR_JUMA_GROUP_NAME,
    WAIT_FOR_BIRTH_GIRL_NAME,
    WAIT_FOR_BIRTH_BOY_NAME,
    START_IMAGE,


    ;

    public static RequestType getRequestType(WaitType waitType) {
        switch (waitType) {
            case WAIT_FOR_JUMA_GROUP_NAME:
            case WAIT_FOR_JUMA_GIRL_NAME:
            case WAIT_FOR_JUMA_BOY_NAME:
                return RequestType.JUMA_CONGRATULATION;
            case WAIT_FOR_NEW_YEAR_BOY_NAME:
            case WAIT_FOR_NEW_YEAR_GIRL_NAME:
                return RequestType.NEW_YEAR_CONGRATULATION;
            case WAIT_FOR_BIRTH_BOY_NAME:
            case WAIT_FOR_BIRTH_GIRL_NAME:
                return RequestType.BIRTH_CONGRATULATION;
            default:
                return null;
        }
    }
}
