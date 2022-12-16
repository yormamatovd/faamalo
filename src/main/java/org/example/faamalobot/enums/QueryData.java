package org.example.faamalobot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryData {
    SET_TIME("set-time", "\uD83D\uDD50 Vaqt sozlamalarini o`rnatish \uD83D\uDD50"),
    COUNTING_NEW_YEAR("count-new-year", "❄️ Yangi yil gacha  ❄"),
    COUNTING_JUMA("count-juma", "🕋️  Juma gacha  🕋️"),

    NEW_YEAR_CONGRATULATE("new-year-congratulate", "☃️Yangi yil tabrigi ☃"),
    JUMA_CONGRATULATE("juma-congratulate", "🕋️ Juma ayyomi tabrigi 🕋"),
    BIRTHDAY_CONGRATULATE("birthday-congratulate", "\uD83C\uDF89 Tug`ilgan kun tabrigi \uD83C\uDF89"),

    MAIN_MENU_NEW_YEAR("main-menu", "\uD83D\uDD3B☃️Asosiy menyu ☃\uD83D\uDD3B"),
    MAIN_MENU_ISLAMIC("main-menu", "\uD83D\uDD3B🕋️ ️Asosiy menyu 🕋️\uD83D\uDD3B"),
    MAIN_MENU_BIRTH("main-menu", "\uD83D\uDD3B\uD83C\uDF89️Asosiy menyu \uD83C\uDF89\uD83D\uDD3B"),

    JUMA_GIRL("juma-congratulate-girl", "\uD83D\uDC71\uD83C\uDFFB\u200D♀ Qiz bolaga \uD83D\uDC71\uD83C\uDFFB\u200D♀"),
    JUMA_BOY("juma-congratulate-boy", "\uD83E\uDDD1\u200D\uD83E\uDDB1 O`gil bolaga \uD83E\uDDD1\u200D\uD83E\uDDB1"),
    JUMA_GROUP("juma-congratulate-group", "\uD83D\uDC65 Jamoatga \uD83D\uDC65"),

    NEW_YEAR_GIRL("new-year-girl", "\uD83D\uDC71\uD83C\uDFFB\u200D♀️Qiz bolaga \uD83D\uDC71\uD83C\uDFFB\u200D♀"),
    NEW_YEAR_BOY("new-year-boy", "\uD83E\uDDD1\u200D\uD83E\uDDB1 O`gil bolaga \uD83E\uDDD1\u200D\uD83E\uDDB1"),

    BIRTH_GIRL("birth-girl", "\uD83D\uDC71\uD83C\uDFFB\u200D♀️Qiz bolaga \uD83D\uDC71\uD83C\uDFFB\u200D♀"),
    BIRTH_BOY("birth-boy", "\uD83E\uDDD1\u200D\uD83E\uDDB1 O`gil bolaga \uD83E\uDDD1\u200D\uD83E\uDDB1"),

    //todo photo buttons
    NOT_BTN("not-command", "◾"),
    NEXT_BTN("next-photo-", "▶"),
    PREVIEW_BTN("preview-photo-", "◀"),
    COUNTER_BTN("counter-photo", "1/1"),
    SHARE_BTN("share-photo", "\uD83D\uDCE4 Do`stlarga yuborish \uD83D\uDCE4"),
    ;

    private String data;
    private String info;

    public static WaitType getWaitType(QueryData queryData) {

        switch (queryData) {
            case JUMA_BOY:
                return WaitType.WAIT_FOR_JUMA_BOY_NAME;
            case JUMA_GIRL:
                return WaitType.WAIT_FOR_JUMA_GIRL_NAME;
            case JUMA_GROUP:
                return WaitType.WAIT_FOR_JUMA_GROUP_NAME;
            case NEW_YEAR_BOY:
                return WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME;
            case NEW_YEAR_GIRL:
                return WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME;
            default:
                return null;
        }
    }
}
