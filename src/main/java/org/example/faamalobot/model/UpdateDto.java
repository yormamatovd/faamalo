package org.example.faamalobot.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateDto {
    private Long chatId;
    private String firstname;
    private String lastname;
    private String username;
    private Integer messageId;
    private Update update;
    private Location location;
    private boolean isLocation;
    private boolean isCallBackQuery;
    private boolean isTextMessage;
    private List<PhotoSize> photoSizeList;

    private String text;
    private CallbackQuery query;

    public List<PhotoSize> getPhotoSizeList() {
        return photoSizeList;
    }

    public void setPhotoSizeList(List<PhotoSize> photoSizeList) {
        this.photoSizeList = photoSizeList;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public boolean isCallBackQuery() {
        return isCallBackQuery;
    }

    public void isCallBackQuery(boolean callBackQuery) {
        isCallBackQuery = callBackQuery;
    }

    public boolean isTextMessage() {
        return isTextMessage;
    }

    public void isTextMessage(boolean textMessage) {
        isTextMessage = textMessage;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void isLocation(boolean location) {
        isLocation = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CallbackQuery getQuery() {
        return query;
    }

    public void setQuery(CallbackQuery query) {
        this.query = query;
    }


}
