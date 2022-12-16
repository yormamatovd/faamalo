package org.example.faamalobot.service;

import org.example.faamalobot.controller.MyBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageId;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageSender {

    @Autowired
    MyBot myBot;

    public Message send(SendMessage sendMessage) {
        try {
            return myBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CompletableFuture<Message> sendAsync(SendMessage sendMessage) {
        try {
            return myBot.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message send(SendPhoto sendPhoto) {
        try {
            return myBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Serializable edit(EditMessageMedia editMessageMedia) {
        try {
            return myBot.execute(editMessageMedia);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Serializable send(CopyMessage copyMessage) {
        try {
            return myBot.execute(copyMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CompletableFuture<MessageId> copyAsync(CopyMessage copyMessage) {
        try {
            return myBot.executeAsync(copyMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(DeleteMessage deleteMessage) {
        try {
            myBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editBoard(EditMessageReplyMarkup editMessageReplyMarkup) {
        try {
            myBot.executeAsync(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
