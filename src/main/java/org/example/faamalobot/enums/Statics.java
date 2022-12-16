package org.example.faamalobot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.faamalobot.model.TimeDto;

@Getter
@AllArgsConstructor
public enum Statics {
    CMD_START("/start"),
    CMD_STATISTIC("/statistics"),
    CMD_SEND_ASYNC("/send_async"),

    START_MSG_TEXT("Assalomu alaykum \uD83D\uDC4B\nUshbu bot sizga ismingiz tushirilgan tabriklar yasab beradi."),
    GIVE_LOCATION_MSG("Joylashuvni yuborish tugmasini bosing!\nMen sizni joylashuv nuqtangizdan vaqtni aniqlab olaman"),
    TIME_SET_SUCCESSFULLY("✅ O`rnatildi ✅"),

    MAIN_MENU("Asosiy Menyu"),

    IM_WAIT_NAME("Ism yuboring...✏️"),

    PLEASE_WAIT("Iltimos kutib turing...⏳"),

    IM_WAIT_GROUP_NAME("Jamoa nomini yuboring...✏️\nNamuna: \"Hurmatli Sinfdoshlarim\""),

    PHOTO_DESCRIPTION("Quvonch ulashing \uD83C\uDF89 \n\n<pre>----------------------------------------</pre> \n@faamalo_bot"),

    SELECT_TYPE_MAN("\uD83D\uDC47 Tabrik kimga tayyorlanayotganini belgilang \uD83D\uDC47");

    private final String value;

    public static String newYearCountingTemplate(TimeDto timeDto) {
        return "☃️  Yangi Yilga Qolgan Kun Aniqlandi \n☃️  (" + timeDto.getZoneId() + ") vaqti bilan\n" +
                "\n" +
                "☃      " + timeDto.getMonths() + " oy\n" +
                "☃      " + timeDto.getDays() + " kun\n" +
                "☃      " + timeDto.getHours() + " soat\n" +
                "☃      " + timeDto.getMinutes() + " daqiqa\n" +
                "☃      " + timeDto.getSeconds() + " soniya\n" +
                "\n" +
                "❄️ Kirib kelayotgan yangi yil bilan!  @faamalo_bot";
    }

    public static String jumaCountingTemplate(TimeDto timeDto) {
        return "\uD83D\uDD4B️  Kelgusi Juma ayyomiga qolgan vaqt \n\uD83D\uDD4B️  (" + timeDto.getZoneId() + ") vaqti bilan\n" +
                "\n" +
                "🕋      " + timeDto.getDays() + " kun\n" +
                "🕋      " + timeDto.getHours() + " soat\n" +
                "🕋      " + timeDto.getMinutes() + " daqiqa\n" +
                "🕋      " + timeDto.getSeconds() + " soniya\n" +
                "\n" +
                "♥️ Yaqinlashib qolgan Juma ayyomi bilan!  @faamalo_bot";
    }
}
