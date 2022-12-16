package org.example.faamalobot.service;


import org.example.faamalobot.enums.QueryData;
import org.example.faamalobot.enums.RequestType;
import org.example.faamalobot.model.PhotoDto;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    public InlineKeyboardMarkup startBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton btnSetTime = new InlineKeyboardButton();
        btnSetTime.setText(QueryData.SET_TIME.getInfo());
        btnSetTime.setCallbackData(QueryData.SET_TIME.getData());


        InlineKeyboardButton btnCountingTimeNewYear = new InlineKeyboardButton();
        btnCountingTimeNewYear.setText(QueryData.COUNTING_NEW_YEAR.getInfo());
        btnCountingTimeNewYear.setCallbackData(QueryData.COUNTING_NEW_YEAR.getData());

        InlineKeyboardButton btnCountingTimeJuma = new InlineKeyboardButton();
        btnCountingTimeJuma.setText(QueryData.COUNTING_JUMA.getInfo());
        btnCountingTimeJuma.setCallbackData(QueryData.COUNTING_JUMA.getData());

        InlineKeyboardButton btnMakeImageNewYear = new InlineKeyboardButton();
        btnMakeImageNewYear.setText(QueryData.NEW_YEAR_CONGRATULATE.getInfo());
        btnMakeImageNewYear.setCallbackData(QueryData.NEW_YEAR_CONGRATULATE.getData());

        InlineKeyboardButton btnMakeImageJuma = new InlineKeyboardButton();
        btnMakeImageJuma.setText(QueryData.JUMA_CONGRATULATE.getInfo());
        btnMakeImageJuma.setCallbackData(QueryData.JUMA_CONGRATULATE.getData());

        InlineKeyboardButton btnMakeImageBirth = new InlineKeyboardButton();
        btnMakeImageBirth.setText(QueryData.BIRTHDAY_CONGRATULATE.getInfo());
        btnMakeImageBirth.setCallbackData(QueryData.BIRTHDAY_CONGRATULATE.getData());


        rowList.add(new ArrayList<>(List.of(btnSetTime)));
        rowList.add(new ArrayList<>(List.of(btnCountingTimeJuma)));

        rowList.add(new ArrayList<>(List.of(btnMakeImageJuma)));
        rowList.add(new ArrayList<>(List.of(btnMakeImageNewYear)));
        rowList.add(new ArrayList<>(List.of(btnMakeImageBirth)));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton mainMenuNewYearBtn() {
        return new InlineKeyboardButton(QueryData.MAIN_MENU_NEW_YEAR.getInfo(), null,
                QueryData.MAIN_MENU_NEW_YEAR.getData(),
                null, null, null, null, null);
    }

    private InlineKeyboardButton mainMenuIslamicBtn() {
        return new InlineKeyboardButton(QueryData.MAIN_MENU_ISLAMIC.getInfo(), null,
                QueryData.MAIN_MENU_ISLAMIC.getData(),
                null, null, null, null, null);
    }

    private InlineKeyboardButton mainMenuBirthBtn() {
        return new InlineKeyboardButton(QueryData.MAIN_MENU_BIRTH.getInfo(), null,
                QueryData.MAIN_MENU_BIRTH.getData(),
                null, null, null, null, null);
    }

    public InlineKeyboardMarkup mainMenuNewYear() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(new ArrayList<>(List.of(mainMenuNewYearBtn())));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup mainMenuIslamic() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(new ArrayList<>(List.of(mainMenuIslamicBtn())));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup mainMenuBirthday() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(new ArrayList<>(List.of(mainMenuBirthBtn())));
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup photoBoard(RequestType requestType, PhotoDto photoDto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        InlineKeyboardButton notBtn = new InlineKeyboardButton();
        notBtn.setText(QueryData.NOT_BTN.getInfo());
        notBtn.setCallbackData(QueryData.NOT_BTN.getData());

        InlineKeyboardButton previewBtn = new InlineKeyboardButton();
        previewBtn.setText(QueryData.PREVIEW_BTN.getInfo());
        previewBtn.setCallbackData(QueryData.PREVIEW_BTN.getData() + (photoDto.getHasPreview() ? photoDto.getPreviewImgId() : ""));

        InlineKeyboardButton nextBtn = new InlineKeyboardButton();
        nextBtn.setText(QueryData.NEXT_BTN.getInfo());
        nextBtn.setCallbackData(QueryData.NEXT_BTN.getData() + (photoDto.getHasNext() ? photoDto.getNextImgId() : ""));

        InlineKeyboardButton counterBtn = new InlineKeyboardButton();
        counterBtn.setText(photoDto.getIndex() + "/" + photoDto.getTotal());
        counterBtn.setCallbackData(QueryData.COUNTER_BTN.getData());

        //todo photo board first row
        if (photoDto.getHasPreview() && photoDto.getHasNext()) {
            rowList.add(List.of(previewBtn, counterBtn, nextBtn));
        } else if (photoDto.getHasNext() && !photoDto.getHasPreview()) {
            rowList.add(List.of(notBtn, counterBtn, nextBtn));
        } else if (!photoDto.getHasNext() && photoDto.getHasPreview()) {
            rowList.add(List.of(previewBtn, counterBtn, notBtn));
        } else if (!photoDto.getHasPreview() && !photoDto.getHasNext()) {
            rowList.add(List.of(notBtn, counterBtn, notBtn));
        }

        //todo photo board share row
        InlineKeyboardButton shareBtn = new InlineKeyboardButton();
        shareBtn.setText(QueryData.SHARE_BTN.getInfo());
        shareBtn.setCallbackData(QueryData.SHARE_BTN.getData());
        shareBtn.setUrl("tg://msg_url?url=" + photoDto.getPhotoUrl() + "&text=Salom");
        shareBtn.setSwitchInlineQuery("share");
//        rowList.add(List.of(shareBtn));

        //todo photo board mainMenu row
        if (requestType.equals(RequestType.JUMA_CONGRATULATION)) {
            rowList.add(List.of(mainMenuIslamicBtn()));
        } else if (requestType.equals(RequestType.NEW_YEAR_CONGRATULATION)) {
            rowList.add(List.of(mainMenuNewYearBtn()));
        } else if (requestType.equals(RequestType.BIRTH_CONGRATULATION)) {
            rowList.add(List.of(mainMenuBirthBtn()));
        }


        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup newYearCongratulateBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> buttonsListRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow3 = new ArrayList<>();

        InlineKeyboardButton btnBoy = new InlineKeyboardButton();
        btnBoy.setText(QueryData.NEW_YEAR_BOY.getInfo());
        btnBoy.setCallbackData(QueryData.NEW_YEAR_BOY.getData());
        buttonsListRow1.add(btnBoy);

        InlineKeyboardButton btnGirl = new InlineKeyboardButton();
        btnGirl.setText(QueryData.NEW_YEAR_GIRL.getInfo());
        btnGirl.setCallbackData(QueryData.NEW_YEAR_GIRL.getData());
        buttonsListRow2.add(btnGirl);

        InlineKeyboardButton btnMainMenu = new InlineKeyboardButton();
        btnMainMenu.setText(QueryData.MAIN_MENU_NEW_YEAR.getInfo());
        btnMainMenu.setCallbackData(QueryData.MAIN_MENU_NEW_YEAR.getData());
        buttonsListRow3.add(btnMainMenu);

        rowList.add(buttonsListRow1);
        rowList.add(buttonsListRow2);
        rowList.add(buttonsListRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup birthdayCongratulateBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> buttonsListRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow3 = new ArrayList<>();

        InlineKeyboardButton btnBoy = new InlineKeyboardButton();
        btnBoy.setText(QueryData.BIRTH_BOY.getInfo());
        btnBoy.setCallbackData(QueryData.BIRTH_BOY.getData());
        buttonsListRow1.add(btnBoy);

        InlineKeyboardButton btnGirl = new InlineKeyboardButton();
        btnGirl.setText(QueryData.BIRTH_GIRL.getInfo());
        btnGirl.setCallbackData(QueryData.BIRTH_GIRL.getData());
        buttonsListRow2.add(btnGirl);

        InlineKeyboardButton btnMainMenu = new InlineKeyboardButton();
        btnMainMenu.setText(QueryData.MAIN_MENU_BIRTH.getInfo());
        btnMainMenu.setCallbackData(QueryData.MAIN_MENU_BIRTH.getData());
        buttonsListRow3.add(btnMainMenu);

        rowList.add(buttonsListRow1);
        rowList.add(buttonsListRow2);
        rowList.add(buttonsListRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup jumaCongratulateBoard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> buttonsListRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow3 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsListRow4 = new ArrayList<>();

        InlineKeyboardButton btnBoy = new InlineKeyboardButton();
        btnBoy.setText(QueryData.JUMA_GIRL.getInfo());
        btnBoy.setCallbackData(QueryData.JUMA_GIRL.getData());
        buttonsListRow1.add(btnBoy);

        InlineKeyboardButton btnGirl = new InlineKeyboardButton();
        btnGirl.setText(QueryData.JUMA_BOY.getInfo());
        btnGirl.setCallbackData(QueryData.JUMA_BOY.getData());
        buttonsListRow2.add(btnGirl);

        InlineKeyboardButton groupJuma = new InlineKeyboardButton();
        groupJuma.setText(QueryData.JUMA_GROUP.getInfo());
        groupJuma.setCallbackData(QueryData.JUMA_GROUP.getData());
        buttonsListRow3.add(groupJuma);

        InlineKeyboardButton btnMainMenu = new InlineKeyboardButton();
        btnMainMenu.setText(QueryData.MAIN_MENU_ISLAMIC.getInfo());
        btnMainMenu.setCallbackData(QueryData.MAIN_MENU_ISLAMIC.getData());
        buttonsListRow4.add(btnMainMenu);

        rowList.add(buttonsListRow1);
        rowList.add(buttonsListRow2);
        rowList.add(buttonsListRow3);
        rowList.add(buttonsListRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardMarkup sendLocation() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);


        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();

        KeyboardButton btn = new KeyboardButton();
        btn.setRequestLocation(true);
        btn.setText("\uD83D\uDCCD Joylashuvni yuborish \uD83D\uDCCD");
        keyboardFirstRow.add(btn);

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}
