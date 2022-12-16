package org.example.faamalobot.service;

import org.example.faamalobot.entity.Follower;
import org.example.faamalobot.enums.Statics;
import org.example.faamalobot.enums.WaitType;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.example.faamalobot.repo.NameBaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Service
public class TextService {

    Map<Long, Integer> imWaitNameList = new HashMap<>();

    @Autowired
    FollowerRepo followerRepo;

    @Autowired
    NameBaseRepo nameBaseRepo;

    @Autowired
    DefaultService defSer;

    @Autowired
    BoardService boardService;

    @Autowired
    MessageMaker maker;

    @Autowired
    MessageSender sender;

    @Autowired
    ImageService imageService;

    @Autowired
    ImageSenderService imageSenderService;

    @Autowired
    AsyncMessageService asyncMessageService;

    @Value(value = "${admin_username}")
    private String adminUsername;

    public void map(UpdateDto updateDto) {

        Follower follower = null;
        if (updateDto.getText().equals(Statics.CMD_START.getValue())) {
            start(updateDto);
            return;
        } else if (updateDto.getText().equals(Statics.CMD_STATISTIC.getValue()) &&
                adminUsername.equals(updateDto.getUsername())) {

            statistics(updateDto.getChatId());

            return;
        } else if (updateDto.getText().equals(Statics.CMD_SEND_ASYNC.getValue()) &&
                adminUsername.equals(updateDto.getUsername())) {
            asyncMessageService.sendAsyncPosting(updateDto, followerRepo.getAllChatId());
        } else {
            follower = followerRepo.findByChatId(updateDto.getChatId()).orElseGet(() -> defSer.addNewFollower(updateDto));

            //todo agar followerga "iltimos ism yuboring" degan habar yuborilsa uni o`chirish
            removeImWaitNameMessage(updateDto);

            if (follower.getWaitType() != null) {
                imageSenderService.makeAndSendImage(updateDto, follower);
            }

        }

        //todo followerni wait type ni null qilish
        //todo har doim imageSend bulganidan keyin ishlasin
        setFollowerWaitType(updateDto, follower, null);

    }

    //todo set follower`s wait type
    private void setFollowerWaitType(UpdateDto updateDto, Follower follower, WaitType waitType) {
        if (follower == null) {
            follower = followerRepo.findByChatId(updateDto.getChatId()).orElseGet(() -> defSer.addNewFollower(updateDto));
        }
        follower.setWaitType(waitType);
        followerRepo.save(follower);
    }


    //*todo iltimos ism yuboring degan habarni o`chirish
    private void removeImWaitNameMessage(UpdateDto updateDto) {
        Map<Long, Integer> removerList = new HashMap<>();
        imWaitNameList.forEach((chatId, messageId) -> {
            if (updateDto.getChatId().equals(chatId)) {
                sender.delete(maker.makeDelete(chatId, messageId));
                removerList.put(chatId, messageId);
            }
        });
        removerList.forEach((chatId, messageId) -> imWaitNameList.remove(chatId, messageId));
    }


    private void start(UpdateDto updateDto) {
        Follower follower = followerRepo.findByChatId(updateDto.getChatId()).orElseGet(() -> defSer.addNewFollower(updateDto));


        sender.send(maker.make(updateDto.getChatId(), Statics.START_MSG_TEXT.getValue(), boardService.startBoard()));
    }


    public void statistics(Long chatId) {
        String staticsTemplate = "<pre>---------------------------------</pre>\n" + "<b>Followers count:</b> " + followerRepo.count() + "\n" + "<pre>---------------------------------</pre>";

        SendMessage message = maker.make(chatId, staticsTemplate);
        sender.send(message);
    }
}
