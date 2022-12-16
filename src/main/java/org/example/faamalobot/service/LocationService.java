package org.example.faamalobot.service;

import net.iakovlev.timeshape.TimeZoneEngine;
import org.example.faamalobot.entity.Follower;
import org.example.faamalobot.enums.Statics;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Yormamatov Davronbek
 * @since 10.01.2022
 */

@Service
public class LocationService {

    @Autowired
    FollowerRepo followerRepo;

    @Autowired
    MessageMaker maker;

    @Autowired
    MessageSender sender;

    @Autowired
    BoardService boardService;

    private Map<Long, Integer> messageBox = new HashMap<>();

    public void map(UpdateDto updateDto) {

        TimeZoneEngine engine = TimeZoneEngine.initialize();


        Optional<ZoneId> timeZoneOptional = engine.query(updateDto.getLocation().getLatitude(), updateDto.getLocation().getLongitude());

        if (timeZoneOptional.isPresent()) {
            Optional<Follower> followerOptional = followerRepo.findByChatId(updateDto.getChatId());
            if (followerOptional.isPresent()) {
                Follower follower = followerOptional.get();

                follower.setTimeZoneId(timeZoneOptional.get().getId());
                followerRepo.save(follower);

                messageBox.forEach((chatId, messageId) -> {
                    if (Objects.equals(chatId, updateDto.getChatId())) {
                        sender.delete(maker.makeDelete(chatId, messageId));
                    }
                });

                sender.delete(maker.makeDelete(updateDto.getChatId(), updateDto.getMessageId()));

                Message send = sender.send(maker.make(updateDto.getChatId(), Statics.TIME_SET_SUCCESSFULLY.getValue(), new ReplyKeyboardRemove(true, true)));

                sender.delete(maker.makeDelete(updateDto.getChatId(), send.getMessageId()));

                sender.send(maker.make(updateDto.getChatId(), Statics.START_MSG_TEXT.getValue(), boardService.startBoard()));

            }
        }
    }

    public Map<Long, Integer> addMessageForDelete(Long chatId, Integer messageId) {
        messageBox.put(chatId, messageId);
        return messageBox;
    }
}
