package org.example.faamalobot.service;

import org.example.faamalobot.entity.Follower;
import org.example.faamalobot.entity.NameBase;
import org.example.faamalobot.enums.QueryData;
import org.example.faamalobot.enums.Statics;
import org.example.faamalobot.enums.WaitType;
import org.example.faamalobot.model.PhotoDto;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.example.faamalobot.repo.NameBaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CallBackService {

    @Autowired
    FollowerRepo followerRepo;

    @Autowired
    DefaultService defaultService;

    @Autowired
    BoardService boardService;

    @Autowired
    MessageMaker maker;

    @Autowired
    MessageSender sender;

    @Autowired
    TextService textService;

    @Autowired
    NameBaseRepo nameBaseRepo;

    @Autowired
    ImageSenderService imageSenderService;

    @Autowired
    @Lazy
    LocationService locationService;




    public void map(UpdateDto updateDto) {
        String data = updateDto.getQuery().getData();
        if (noAction(data)) return;
        Optional<Follower> followerOptional = followerRepo.findByChatId(updateDto.getChatId());

        //todo remove firsMessageAlbum
        followerOptional.ifPresent(this::removeFirstMessage);

        if (data.startsWith(QueryData.PREVIEW_BTN.getData())) {
            sendNextImage(updateDto, data);
            return;
        } else if (data.startsWith(QueryData.NEXT_BTN.getData())) {
            sendNextImage(updateDto, data);
            return;
        }

        //todo delete clicked message
        sender.delete(maker.makeDelete(updateDto.getChatId(), updateDto.getMessageId()));


        if (data.equals(QueryData.COUNTING_NEW_YEAR.getData())) {
            countingNewYear(updateDto);
        }

        if (data.equals(QueryData.SET_TIME.getData())) {
            Message send = sender.send(maker.make(updateDto.getChatId(), Statics.GIVE_LOCATION_MSG.getValue(), boardService.sendLocation()));
            locationService.addMessageForDelete(updateDto.getChatId(), send.getMessageId());
            System.out.println(locationService);
        }

        if (data.equals(QueryData.COUNTING_JUMA.getData())) {
            countingJuma(updateDto);

        } else if (data.equals(QueryData.NEW_YEAR_CONGRATULATE.getData())) {
            congratulateNewYearStart(updateDto);
        } else if (data.equals(QueryData.JUMA_CONGRATULATE.getData())) {
            congratulateJumaStart(updateDto);
        } else if (data.equals(QueryData.BIRTHDAY_CONGRATULATE.getData())) {
            congratulateBirthdayStart(updateDto);


        } else if (data.equals(QueryData.NEW_YEAR_BOY.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME);
        } else if (data.equals(QueryData.NEW_YEAR_GIRL.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME);

        } else if (data.equals(QueryData.JUMA_GIRL.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_JUMA_GIRL_NAME);
        } else if (data.equals(QueryData.JUMA_BOY.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_JUMA_BOY_NAME);
        } else if (data.equals(QueryData.JUMA_GROUP.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_JUMA_GROUP_NAME);


        } else if (data.equals(QueryData.BIRTH_BOY.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_BIRTH_BOY_NAME);
        } else if (data.equals(QueryData.BIRTH_GIRL.getData())) {
            sendCongratulate(updateDto, WaitType.WAIT_FOR_BIRTH_GIRL_NAME);

        } else if (data.equals(QueryData.MAIN_MENU_ISLAMIC.getData())) {
            mainMenu(updateDto);
        }
    }


    private boolean noAction(String data) {
        return data.equals(QueryData.COUNTER_BTN.getData()) ||
                data.equals(QueryData.NOT_BTN.getData());
    }

    private void sendNextImage(UpdateDto updateDto, String data) {

        try {
            Long nextImageId = Long.valueOf(data.substring(data.lastIndexOf("-") + 1));
            List<NameBase> images = imageSenderService.getImageFriendsByOneId(nextImageId);
            images.sort(Comparator.comparing(NameBase::getId));

            Optional<NameBase> nextImageOptional = nameBaseRepo.findById(nextImageId);
            if (nextImageOptional.isEmpty()) return;
            NameBase activeImage = nextImageOptional.get();

            PhotoDto photoDto = new PhotoDto();
            photoDto.setTotal(images.size());
            photoDto.setFileId(activeImage.getFileId());


            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).getId().equals(activeImage.getId())) {
                    if (i != 0) {
                        photoDto.setIndex(i + 1L);
                        photoDto.setPreviewImgId(images.get(i - 1).getId());
                        photoDto.setHasPreview(true);
                    } else {
                        photoDto.setIndex(i + 1L);
                        photoDto.setPreviewImgId(0L);
                        photoDto.setHasPreview(false);
                    }
                    if (i + 1 != images.size()) {
                        photoDto.setIndex(i + 1L);
                        photoDto.setHasNext(true);
                        photoDto.setNextImgId(images.get(i + 1).getId());
                    } else {
                        photoDto.setIndex(i + 1L);
                        photoDto.setHasNext(false);
                        photoDto.setNextImgId(0L);
                    }
                }
            }


            EditMessageMedia editMessageMedia = new EditMessageMedia();
            editMessageMedia.setMessageId(updateDto.getMessageId());
            editMessageMedia.setChatId(updateDto.getChatId() + "");
            editMessageMedia.setReplyMarkup(
                    boardService.photoBoard(
                            WaitType.getRequestType(activeImage.getType()),
                            photoDto
                    )
            );
            InputMediaPhoto mediaPhoto = new InputMediaPhoto(activeImage.getFileId());
            mediaPhoto.setCaption(Statics.PHOTO_DESCRIPTION.getValue());
            mediaPhoto.setParseMode(ParseMode.HTML);

            editMessageMedia.setMedia(mediaPhoto);

            sender.edit(editMessageMedia);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeFirstMessage(Follower follower) {

        if (follower.getFirstMessageId() != null) {
            if (follower.getFirstMessageId().length() > 0) {
                String[] split = follower.getFirstMessageId().split(",");
                for (String s : split) {
                    try {
                        sender.delete(maker.makeDelete(follower.getChatId(), Integer.parseInt(s)));
                    } catch (Exception ignored) {
                    }
                }
                follower.setFirstMessageId("");
                followerRepo.save(follower);
            }
        }
    }

    private void sendCongratulate(UpdateDto updateDto, WaitType waitType) {
        Follower follower = followerRepo.findByChatId(updateDto.getChatId()).orElseGet(() -> defaultService.addNewFollower(updateDto));
        follower.setWaitType(waitType);
        followerRepo.save(follower);

        Message send;
        if (waitType.equals(WaitType.WAIT_FOR_JUMA_GROUP_NAME)) {
            send = sender.send(maker.make(updateDto.getChatId(), Statics.IM_WAIT_GROUP_NAME.getValue()));
        } else {
            send = sender.send(maker.make(updateDto.getChatId(), Statics.IM_WAIT_NAME.getValue()));
        }
        textService.imWaitNameList.put(updateDto.getChatId(), send.getMessageId());
    }

    private void mainMenu(UpdateDto updateDto) {
        sender.send(maker.make(updateDto.getChatId(), Statics.MAIN_MENU.getValue(), boardService.startBoard()));
    }

    private void congratulateNewYearStart(UpdateDto updateDto) {
        sender.send(
                maker.make(
                        updateDto.getChatId(),
                        Statics.SELECT_TYPE_MAN.getValue(),
                        boardService.newYearCongratulateBoard()
                )
        );
    }

    private void congratulateJumaStart(UpdateDto updateDto) {
        sender.send(
                maker.make(
                        updateDto.getChatId(),
                        Statics.SELECT_TYPE_MAN.getValue(),
                        boardService.jumaCongratulateBoard()
                )
        );
    }

    private void congratulateBirthdayStart(UpdateDto updateDto) {
        sender.send(
                maker.make(
                        updateDto.getChatId(),
                        Statics.SELECT_TYPE_MAN.getValue(),
                        boardService.birthdayCongratulateBoard()
                )
        );
    }

    public void countingNewYear(UpdateDto updateDto) {
        LocalDateTime fromDateTime;


        Optional<Follower> followerOptional = followerRepo.findByChatId(updateDto.getChatId());
        String zoneId = null;
        if (followerOptional.isPresent()) {
            Follower follower = followerOptional.get();
            zoneId = follower.getTimeZoneId();
        }

        if (zoneId != null) {
            fromDateTime = LocalDateTime.now(ZoneId.of(zoneId));
        } else {
            fromDateTime = LocalDateTime.now(ZoneOffset.UTC);
        }

        LocalDateTime toDateTime = LocalDateTime.of(fromDateTime.getYear() + 1, Month.JANUARY, 1, 0, 0, 0);


        String readyTemplate = Statics.newYearCountingTemplate(defaultService.getBetweenTwoTime(fromDateTime, toDateTime, zoneId));

        sender.send(maker.make(updateDto.getChatId(), readyTemplate, boardService.mainMenuNewYear()));
    }

    public void countingJuma(UpdateDto updateDto) {
        LocalDateTime fromDateTime;
        Optional<Follower> followerOptional = followerRepo.findByChatId(updateDto.getChatId());

        String zoneId = null;
        if (followerOptional.isPresent()) {
            Follower follower = followerOptional.get();
            zoneId = follower.getTimeZoneId();
        }

        if (zoneId != null) {
            fromDateTime = LocalDateTime.now(ZoneId.of(zoneId));
        } else {
            fromDateTime = LocalDateTime.now(ZoneOffset.UTC);
        }

        LocalDateTime toDateTime = fromDateTime.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        toDateTime = toDateTime.withHour(0);
        toDateTime = toDateTime.withMinute(0);
        toDateTime = toDateTime.withSecond(0);

        String readyTemplate = Statics.jumaCountingTemplate(defaultService.getBetweenTwoTime(fromDateTime, toDateTime, zoneId));

        sender.send(maker.make(updateDto.getChatId(), readyTemplate, boardService.mainMenuIslamic()));
    }

}
