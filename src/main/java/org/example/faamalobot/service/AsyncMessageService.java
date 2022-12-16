package org.example.faamalobot.service;

import org.example.faamalobot.controller.MyBot;
import org.example.faamalobot.model.AsyncBoardDto;
import org.example.faamalobot.model.UpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

/**
 * @author Yormamatov Davronbek
 * @since 29.01.2022
 */

@Service
public class AsyncMessageService {

    @Autowired
    @Lazy
    private MyBot myBot;

    private AsyncBoardDto boardDto = new AsyncBoardDto(false, false, 0, 0, null, 0);

    private boolean waitCountFromAdmin = false;
    private boolean waitPostIDFromAdmin = false;
    private boolean waitUsernameFromAdmin = false;
    private int lastBoardId = 0;

    public boolean isWaitCountFromAdmin() {
        return waitCountFromAdmin;
    }

    public int getLastBoardId() {
        return lastBoardId;
    }

    public void setLastBoardId(int lastBoardId) {
        this.lastBoardId = lastBoardId;
    }

    public void setWaitCountFromAdmin(boolean waitCountFromAdmin) {
        this.waitCountFromAdmin = waitCountFromAdmin;
    }

    public boolean isWaitPostIDFromAdmin() {
        return waitPostIDFromAdmin;
    }

    public void setWaitPostIDFromAdmin(boolean waitPostIDFromAdmin) {
        this.waitPostIDFromAdmin = waitPostIDFromAdmin;
    }

    public boolean isWaitUsernameFromAdmin() {
        return waitUsernameFromAdmin;
    }

    public void setWaitUsernameFromAdmin(boolean waitUsernameFromAdmin) {
        this.waitUsernameFromAdmin = waitUsernameFromAdmin;
    }

    public long getSendFollowerCount() {
        return boardDto.getSendFollowerCount();
    }

    public AsyncBoardDto setFollowerCountForSend(long count) {
        boardDto.setSendFollowerCount(count);
        return boardDto;
    }

    public AsyncBoardDto setPostId(int postId) {
        boardDto.setPostId(postId);
        return boardDto;
    }

    public AsyncBoardDto setChannelUsername(String channelUsername) {
        boardDto.setChannelUsername(channelUsername);
        return boardDto;
    }

    public AsyncBoardDto setDescState() {
        boardDto.setSendByDesc(!boardDto.isSendByDesc());
        return boardDto;
    }

    public AsyncBoardDto setNotification() {
        boardDto.setNotification(!boardDto.isNotification());
        return boardDto;
    }

    public AsyncBoardDto setAllFollowerCount(List<Long> followersChatIdList) {
        boardDto.setAllFollowerCount(followersChatIdList.size());
        return boardDto;
    }


    public void testPost(UpdateDto updateDto) {
        CopyMessage copyMessage = copyOnlyPost(boardDto.getChannelUsername(), boardDto.getPostId(), updateDto.getChatId());
        try {
            myBot.executeAsync(copyMessage);
            myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Click /async_send_start for start process"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Channel username or post ID noto`g`ri : https://t.me/" + boardDto.getChannelUsername() + "/" + boardDto.getPostId()));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateBoard(UpdateDto updateDto) {
        if (boardDto.getChannelUsername() == null && boardDto.getPostId() == 0) {
            try {
                myBot.executeAsync(
                        new EditMessageText(
                                updateDto.getChatId() + "",
                                getLastBoardId(),
                                null,
                                "Kanal username va post ID ni to`g`ri kiriting",
                                ParseMode.HTML,
                                true,
                                asyncMessageToMembersBoard(boardDto),
                                null
                        )
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (boardDto.getChannelUsername() != null && boardDto.getPostId() == 0) {
            try {
                myBot.executeAsync(
                        new EditMessageText(
                                updateDto.getChatId() + "",
                                getLastBoardId(),
                                null,
                                "Post ID ni to`g`ri kiriting",
                                ParseMode.HTML,
                                true,
                                asyncMessageToMembersBoard(boardDto),
                                null
                        )
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (boardDto.getChannelUsername() == null && boardDto.getPostId() != 0) {
            try {
                myBot.executeAsync(
                        new EditMessageText(
                                updateDto.getChatId() + "",
                                getLastBoardId(),
                                null,
                                "Kanal username ni to`g`ri kiriting",
                                ParseMode.HTML,
                                true,
                                asyncMessageToMembersBoard(boardDto),
                                null
                        )
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (boardDto.getChannelUsername() != null && boardDto.getPostId() != 0) {
            try {
                myBot.executeAsync(
                        new CopyMessage(
                                updateDto.getChatId() + "",
                                boardDto.getChannelUsername(),
                                boardDto.getPostId(),
                                null,
                                ParseMode.HTML,
                                null,
                                true,
                                null,
                                true,
                                asyncMessageToMembersBoard(boardDto)
                        )
                );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendAsyncPosting(UpdateDto updateDto, Set<Long> allFollowersChatIdSet) {
        boardDto = new AsyncBoardDto(false, false, 0, allFollowersChatIdSet.size(), null, 0);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(updateDto.getChatId() + "");
        sendMessage.setReplyToMessageId(updateDto.getMessageId());
        sendMessage.setText("Kanal username va post ID ni to`g`ri kiriting");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(asyncMessageToMembersBoard(boardDto));
        try {
            myBot.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void startPosting(Set<Long> chatIdList, UpdateDto updateDto) {
        if (boardDto.getChannelUsername() == null && boardDto.getPostId() == 0) {
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Kanal username va post ID ni to`g`ri kiriting"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        if (boardDto.getChannelUsername() == null && boardDto.getPostId() != 0) {
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Kanal username ni to`g`ri kiriting"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        if (boardDto.getChannelUsername() != null && boardDto.getPostId() == 0) {
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Post ID ni to`g`ri kiriting"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        if (boardDto.getSendFollowerCount() == 0) {
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Yuborilishi kerak bo`lgan obunachilar soni noto`g`ri"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
        if (boardDto.getSendFollowerCount() > chatIdList.size()) {
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Yuborilishi kerak bo`lgan obunachilar soni jami obunachilar sonidan ko`p "));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }


        try {
            CopyMessage copyMessage = copyOnlyPost(boardDto.getChannelUsername(), boardDto.getPostId(), updateDto.getChatId());
            myBot.executeAsync(copyMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Channel username or post ID noto`g`ri : https://t.me/" + boardDto.getChannelUsername() + "/" + boardDto.getPostId()));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
            return;
        }

        MessageThread messageThread = new MessageThread();
        messageThread.setProps(boardDto.getChannelUsername(), boardDto.getPostId(), "thredMessage", new HashSet<>(chatIdList), boardDto.isNotification(), updateDto.getChatId(),myBot);
        messageThread.start();


    }

    private CopyMessage copyOnlyPost(String channelUsername, Integer postId, Long chatId) {
        CopyMessage copyMessage = new CopyMessage(chatId + "", channelUsername, postId);
        copyMessage.setParseMode(ParseMode.HTML);
        return copyMessage;
    }

    private InlineKeyboardMarkup asyncMessageToMembersBoard(AsyncBoardDto dto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> notifyRow = new ArrayList<>();
        List<InlineKeyboardButton> descRow = new ArrayList<>();
        List<InlineKeyboardButton> countRow = new ArrayList<>();
        List<InlineKeyboardButton> usernameRow = new ArrayList<>();
        List<InlineKeyboardButton> postIdRow = new ArrayList<>();
        List<InlineKeyboardButton> testRow = new ArrayList<>();

        InlineKeyboardButton notification = new InlineKeyboardButton();
        notification.setText("Ovoz bilan borsin: " + (dto.isNotification() ? "✅" : "❌"));
        notification.setCallbackData("async-notif");
        notifyRow.add(notification);

        InlineKeyboardButton desc = new InlineKeyboardButton();
        desc.setText("Teskari hisobda yuborish: " + (dto.isSendByDesc() ? "✅" : "❌"));
        desc.setCallbackData("async-desc");
        descRow.add(desc);

        InlineKeyboardButton sendingCount = new InlineKeyboardButton();
        sendingCount.setText("Nechta followerga yuborilsin : " + dto.getAllFollowerCount() + " | " + dto.getSendFollowerCount());
        sendingCount.setCallbackData("async-count");
        countRow.add(sendingCount);

        InlineKeyboardButton channelUsername = new InlineKeyboardButton();
        channelUsername.setText("Kanal username : " + dto.getChannelUsername());
        channelUsername.setCallbackData("async-channel-username");
        usernameRow.add(channelUsername);

        InlineKeyboardButton postId = new InlineKeyboardButton();
        postId.setText("Post id : " + dto.getPostId());
        postId.setCallbackData("async-channel-post-id");
        postIdRow.add(postId);

        InlineKeyboardButton test = new InlineKeyboardButton();
        test.setText("TEST POST");
        test.setCallbackData("async-test-send-post");
        testRow.add(test);

        rowList.add(notifyRow);
        rowList.add(descRow);
        rowList.add(countRow);
        rowList.add(usernameRow);
        rowList.add(postIdRow);
        rowList.add(testRow);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public void requestFollowerCount(UpdateDto updateDto) {
        try {
            myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Nechta obunachiga yuborilishi kerakligini yuboring: "));
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void requestChannelUsername(UpdateDto updateDto) {
        try {
            myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Kanal username sini @ belgi bilan yuboring: "));
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void requestPostId(UpdateDto updateDto) {
        try {
            myBot.executeAsync(new SendMessage(updateDto.getChatId() + "", "Copy qilinishi kerak bo`lgan Post ID ni yuboring: "));
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}

