package org.example.faamalobot.service;

import org.example.faamalobot.controller.MyBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.Set;

/**
 * @author Yormamatov Davronbek
 * @since 02.02.2022
 */
@Service
class MessageThread implements Runnable {

    private MyBot myBot;
    private Thread thread;
    private String threadName;
    private String channelUsername;
    private Integer postId;
    private boolean notification;
    private long adminChatId;
    private Set<Long> chatIdSet;


    @Override
    public void run() {

        long sended = 0, wait = chatIdSet.size(), conflict = 0;
        Message execute = null;
        try {
            execute = myBot.execute(new SendMessage(adminChatId + "",
                    "Yuborish boshlandi\nYuborilganlar: " + sended + "\nNavbatdagilar: " + wait + "\nXatolik: " + conflict));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


        for (Long chatId : chatIdSet) {
            try {
                CopyMessage copyMessage = copyOnlyPost(channelUsername, postId, chatId, notification);
                myBot.executeAsync(copyMessage);
                sended++;
                wait--;
            } catch (TelegramApiException e) {
                e.printStackTrace();
                conflict++;
                wait--;
            }
            try {
                myBot.executeAsync(new EditMessageText(adminChatId + "", Objects.requireNonNull(execute).getMessageId(), null,
                        "Yuborish boshlandi\nYuborilganlar: " + sended + "\nNavbatdagilar: " + wait + "\nXatolik: " + conflict,
                        ParseMode.HTML, true, null, null));
            } catch (TelegramApiException e) {
                e.printStackTrace();
                try {
                    execute = myBot.execute(new SendMessage(adminChatId + "",
                            "Yuborish davom etmoqda\nYuborilganlar: " + sended + "\nNavbatdagilar: " + wait + "\nXatolik: " + conflict));
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            myBot.execute(new EditMessageText(adminChatId + "", Objects.requireNonNull(execute).getMessageId(), null,
                    "Yuborish tugatildi\nYuborilganlar: " + sended + "\nNavbatdagilar: " + wait + "\nXatolik: " + conflict,
                    ParseMode.HTML, true, null, null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        clearProps();
    }

    public CopyMessage copyOnlyPost(String channelUsername, Integer postId, Long chatId, boolean notification) {
        CopyMessage copyMessage = new CopyMessage(chatId + "", channelUsername, postId);
        copyMessage.setParseMode(ParseMode.HTML);
        copyMessage.setDisableNotification(!notification);
        return copyMessage;
    }

    public void setProps(String channelUsername, Integer postId, String threadName, Set<Long> chatIdSet,
                         boolean notification, long adminChatId, MyBot myBot) {
        this.channelUsername = channelUsername;
        this.postId = postId;
        this.threadName = threadName;
        this.chatIdSet = chatIdSet;
        this.notification = notification;
        this.adminChatId = adminChatId;
        this.myBot = myBot;
    }

    public void clearProps() {
        this.channelUsername = null;
        this.postId = null;
        this.threadName = null;
        this.chatIdSet = null;
        this.notification = false;
    }

    public void start() {
        System.out.println("Thread started");
        if (thread != null) {
            if (!thread.isAlive()) {
                if (this.threadName == null ||
                        this.chatIdSet == null ||
                        this.channelUsername == null ||
                        this.postId == null) {
                    //todo something wrong with fields
                    return;
                }

                thread = new Thread(this, this.threadName);
                thread.start();
            } else {
                //todo thread busy
            }
        } else {
            if (this.threadName == null ||
                    this.chatIdSet == null ||
                    this.channelUsername == null ||
                    this.postId == null) {
                //todo something wrong with fields
                return;
            }

            thread = new Thread(this, this.threadName);
            thread.start();
        }
    }
}
