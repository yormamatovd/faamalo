package org.example.faamalobot.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.example.faamalobot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class MyBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${admin_username}")
    private String adminUsername;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    @Lazy
    CallBackService callBackService;

    @Autowired
    @Lazy
    TextService textService;

    @Autowired
    @Lazy
    LocationService locationService;

    @Autowired
    @Lazy
    DefaultService defaultService;

    @Autowired
    @Lazy
    AsyncMessageService asyncMessageService;

    @Autowired
    FollowerRepo followerRepo;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                UpdateDto updateDto = getUpdateDtoFromMessage(update);
                if (asyncMethod(updateDto)) return;
                defaultService.receiveAction(updateDto);
                textService.map(updateDto);
            } else if (update.getMessage().hasLocation()) {
                UpdateDto updateDto = getUpdateDtoFromMessageLocation(update);
                defaultService.receiveAction(updateDto);
                locationService.map(updateDto);
            }
        } else if (update.hasCallbackQuery()) {
            UpdateDto updateDto = getUpdateDtoFromCallBackQuery(update);
            if (asyncMethod(updateDto)) return;
            defaultService.receiveAction(updateDto);
            callBackService.map(updateDto);
        }
    }

    private boolean asyncMethod(UpdateDto updateDto) {
        if (updateDto.getUsername()==null)return false;
        if (updateDto.getUsername().equals(adminUsername)) {
            if (updateDto.isTextMessage()) {
                if (asyncMessageService.isWaitCountFromAdmin()) {
                    if (isAllDigit(updateDto.getText())) {
                        asyncMessageService.setFollowerCountForSend(Long.parseLong(updateDto.getText()));
                        asyncMessageService.setWaitCountFromAdmin(false);
                        asyncMessageService.updateBoard(updateDto);
                        return true;
                    } else {
                        //todo count btn clicked
                        asyncMessageService.requestFollowerCount(updateDto);
                        asyncMessageService.setWaitCountFromAdmin(true);
                        return true;
                    }
                } else if (asyncMessageService.isWaitPostIDFromAdmin()) {
                    if (isAllDigit(updateDto.getText())) {
                        asyncMessageService.setPostId(Integer.parseInt(updateDto.getText()));
                        asyncMessageService.setWaitPostIDFromAdmin(false);
                        asyncMessageService.updateBoard(updateDto);
                        return true;
                    } else {
                        //todo count btn clicked
                        asyncMessageService.requestPostId(updateDto);
                        asyncMessageService.setWaitPostIDFromAdmin(true);
                        return true;
                    }
                } else if (asyncMessageService.isWaitUsernameFromAdmin()) {
                    if (updateDto.getText().startsWith("@")) {
                        asyncMessageService.setChannelUsername(updateDto.getText());
                        asyncMessageService.setWaitUsernameFromAdmin(false);
                        asyncMessageService.updateBoard(updateDto);
                        return true;
                    } else {
                        //todo count btn clicked
                        asyncMessageService.requestChannelUsername(updateDto);
                        asyncMessageService.setWaitUsernameFromAdmin(true);
                        return true;
                    }
                } else if (updateDto.getText().equals("/async_send_start")) {
                    asyncMessageService.startPosting(
                            followerRepo.getFollowersLimit(asyncMessageService.getSendFollowerCount()),
                            updateDto
                    );
                    return true;
                }
                return false;
            } else if (updateDto.isCallBackQuery()) {
                if (updateDto.getQuery().getData().equals("async-notif")) {
                    //todo notify btn clicked
                    asyncMessageService.setNotification();
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    asyncMessageService.updateBoard(updateDto);
                    return true;
                } else if (updateDto.getQuery().getData().equals("async-desc")) {
                    //todo desc btn clicked
                    asyncMessageService.setDescState();
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    asyncMessageService.updateBoard(updateDto);
                    return true;
                } else if (updateDto.getQuery().getData().equals("async-count")) {
                    //todo count btn clicked
                    asyncMessageService.requestFollowerCount(updateDto);
                    asyncMessageService.setWaitCountFromAdmin(true);
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    return true;
                } else if (updateDto.getQuery().getData().equals("async-channel-username")) {
                    //todo username btn clicked
                    asyncMessageService.requestChannelUsername(updateDto);
                    asyncMessageService.setWaitUsernameFromAdmin(true);
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    return true;
                } else if (updateDto.getQuery().getData().equals("async-channel-post-id")) {
                    //todo post id btn clicked
                    asyncMessageService.requestPostId(updateDto);
                    asyncMessageService.setWaitPostIDFromAdmin(true);
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    return true;
                } else if (updateDto.getQuery().getData().equals("async-test-send-post")) {
                    //todo test btn clicked
                    asyncMessageService.testPost(updateDto);
                    asyncMessageService.setLastBoardId(updateDto.getMessageId());
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean isAllDigit(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) return false;
        }
        return true;
    }


    private UpdateDto getUpdateDtoFromMessage(Update update) {
        UpdateDto updateDto = new UpdateDto();
        updateDto.setUpdate(update);

        updateDto.setChatId(update.getMessage().getChatId());
        updateDto.setMessageId(update.getMessage().getMessageId());

        updateDto.isTextMessage(true);
        updateDto.isCallBackQuery(false);

        updateDto.setText(update.getMessage().getText());
        updateDto.setFirstname(update.getMessage().getChat().getFirstName());
        updateDto.setLastname(update.getMessage().getChat().getLastName());
        updateDto.setUsername(update.getMessage().getChat().getUserName());
        return updateDto;
    }

    private UpdateDto getUpdateDtoFromMessageLocation(Update update) {
        UpdateDto updateDto = new UpdateDto();
        updateDto.setUpdate(update);

        updateDto.setChatId(update.getMessage().getChatId());
        updateDto.setMessageId(update.getMessage().getMessageId());

        updateDto.isTextMessage(false);
        updateDto.isCallBackQuery(false);
        updateDto.isLocation(true);

        updateDto.setLocation(update.getMessage().getLocation());

        updateDto.setFirstname(update.getMessage().getChat().getFirstName());
        updateDto.setLastname(update.getMessage().getChat().getLastName());
        updateDto.setUsername(update.getMessage().getChat().getUserName());
        return updateDto;
    }

    private UpdateDto getUpdateDtoFromCallBackQuery(Update update) {


        UpdateDto updateDto = new UpdateDto();
        updateDto.setUpdate(update);

        updateDto.setChatId(update.getCallbackQuery().getMessage().getChatId());
        updateDto.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        updateDto.isTextMessage(false);
        updateDto.isCallBackQuery(true);

        updateDto.setText(update.getCallbackQuery().getMessage().getText());
        updateDto.setFirstname(update.getCallbackQuery().getMessage().getChat().getFirstName());
        updateDto.setLastname(update.getCallbackQuery().getMessage().getChat().getLastName());
        updateDto.setUsername(update.getCallbackQuery().getMessage().getChat().getUserName());

        updateDto.setQuery(update.getCallbackQuery());
        return updateDto;
    }
}
