package org.example.faamalobot.service;

import org.apache.commons.io.FileUtils;
import org.example.faamalobot.entity.Follower;
import org.example.faamalobot.entity.MyFont;
import org.example.faamalobot.entity.NameBase;
import org.example.faamalobot.enums.Statics;
import org.example.faamalobot.enums.WaitType;
import org.example.faamalobot.model.PhotoDto;
import org.example.faamalobot.model.TemplateDto;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.example.faamalobot.repo.FontRepo;
import org.example.faamalobot.repo.NameBaseRepo;
import org.example.faamalobot.repo.TemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class ImageSenderService {

    @Autowired
    BoardService boardService;

    @Autowired
    MessageMaker maker;

    @Autowired
    MessageSender sender;

    @Autowired
    FollowerRepo followerRepo;

    @Autowired
    NameBaseRepo nameBaseRepo;

    @Autowired
    TemplateRepo templateRepo;

    @Autowired
    FontRepo fontRepo;

    @Autowired
    ImageService imageService;
    @Value(value = "${base.channel}")
    private String baseChannelUsername;

    @Value(value = "${ready_send_folder}")
    private String readyToSendFolder;
    @Value(value = "${no_ready_folder}")
    private String noReadyImagesFolder;

    public void makeAndSendImage(UpdateDto updateDto, Follower follower) {

        Message pleaseWait = sender.send(maker.make(updateDto.getChatId(), Statics.PLEASE_WAIT.getValue()));

        List<NameBase> alreadyExistPhotos = nameBaseRepo.findByNameAndType(updateDto.getText(), follower.getWaitType());

        List<NameBase> newMakedPhotos = addToBaseNewPhoto(updateDto, alreadyExistPhotos, follower.getWaitType(), updateDto.getText());

        //todo avvaldan mavjud bo`lgan rasmlarni yangi rasmlar bilan bitta listga jamlab sort qilish qismi
        List<NameBase> readyToSendingList = new ArrayList<>(alreadyExistPhotos);
        readyToSendingList.addAll(newMakedPhotos);
        readyToSendingList.sort(Comparator.comparing(NameBase::getId));


        PhotoDto photoDto = new PhotoDto();
        photoDto.setHasNext(readyToSendingList.size() > 1);
        photoDto.setHasPreview(false);
        photoDto.setTotal(readyToSendingList.size());
        photoDto.setFileId(readyToSendingList.get(0).getFileId());
        photoDto.setPreviewImgId(0L);
        photoDto.setIndex(1L);

        if (photoDto.getHasNext()) {
            photoDto.setNextImgId(readyToSendingList.get(1).getId());
        }

        InlineKeyboardMarkup inlineKeyboardMarkup =
                boardService.photoBoard(WaitType.getRequestType(follower.getWaitType()), photoDto
                );

        SendPhoto make = maker.make(updateDto.getChatId(),
                Statics.PHOTO_DESCRIPTION.getValue(),
                inlineKeyboardMarkup,
                readyToSendingList.get(0).getFileId());
        make.setParseMode(ParseMode.HTML);

        sender.delete(new DeleteMessage(updateDto.getChatId() + "", pleaseWait.getMessageId()));


        sender.send(make);

    }

    private List<NameBase> addToBaseNewPhoto(UpdateDto updateDto, List<NameBase> alreadyExistInBase, WaitType waitType, String text) {
        List<NameBase> newReadyPhotoList = new ArrayList<>();


        List<TemplateDto> templateDtoList = templateRepo.findAllByWaitTypes(waitType.name());


        for (TemplateDto templateDto : templateDtoList) {

            //todo avval chizilgan rasm bo`lsa o`tkazvorish
            boolean cantWriteBecauseAlreadyExist = false;
            for (NameBase inBase : alreadyExistInBase) {
                if (inBase.getTemplateFileId().equals(templateDto.getFileId())) {
                    cantWriteBecauseAlreadyExist = true;
                }
            }
            if (cantWriteBecauseAlreadyExist) continue;

            //todo rasmni yuklab olib BufferedImage ga olish qismi (bu template)
            BufferedImage tempBuffImg = null;
            try {
                System.err.println(noReadyImagesFolder);
                System.err.println(templateDto.getFileId());
                System.err.println(noReadyImagesFolder + templateDto.getFileId());
                tempBuffImg = ImageIO.read(Objects.requireNonNull(getFileFromResourceAsStream(noReadyImagesFolder + templateDto.getFileId())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //todo tayyor chisilgan rasm
            String printedImagePath = null;
            if (tempBuffImg != null) {
                printedImagePath = getPrintedImagePath(text, tempBuffImg, templateDto);
            }

            if (printedImagePath != null) {
                //todo rasmni kanalga yuborish
                SendPhoto sendPhoto = maker.makeSendPhoto(baseChannelUsername, printedImagePath);
                sendPhoto.setCaption((updateDto.getUsername() != null ?
                        "@" + updateDto.getUsername() + "    -    " + updateDto.getText() :
                        updateDto.getFirstname() + "  /  " + updateDto.getLastname() + "    -    " + updateDto.getText()));
                Message send = sender.send(sendPhoto);

                NameBase nameBase = new NameBase(
                        send.getPhoto().get(send.getPhoto().size() - 1).getFileId(),
                        text,
                        templateDto.getFileId(),
                        waitType);

                newReadyPhotoList.add(nameBaseRepo.save(nameBase));
                while (send.getMessageId() == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //todo delete saved photo
                deleteFile(printedImagePath);
            }
        }
        return newReadyPhotoList;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }


    private String getPrintedImagePath(String text, BufferedImage tempBuffImg, TemplateDto templateDto) {


        MyFont myFont = fontRepo.findByFontCode(templateDto.getFontCode());


        BufferedImage readyBufferedImage = imageService.templateN1(text, myFont, tempBuffImg, templateDto);


        if (readyBufferedImage != null) {
            String readyImagePath = readyToSendFolder  + UUID.randomUUID() + ".jpeg";

            try {
                ImageIO.write(readyBufferedImage, "jpeg", new File(readyImagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readyImagePath;
        }
        return null;
    }

    private void deleteFile(String path) {
        try {
            FileUtils.forceDelete(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<NameBase> getImageFriendsByOneId(Long id) {
        Optional<NameBase> byId = nameBaseRepo.findById(id);
        if (byId.isPresent()) {
            NameBase active = byId.get();
            return nameBaseRepo.findByNameAndType(active.getName(), active.getType());
        }
        return null;
    }
}


