package org.example.faamalobot.service;


import org.example.faamalobot.entity.Follower;
import org.example.faamalobot.model.TimeDto;
import org.example.faamalobot.model.UpdateDto;
import org.example.faamalobot.repo.FollowerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class DefaultService {

    @Autowired
    FollowerRepo followerRepo;

    public void receiveAction(UpdateDto updateDto) {

        Optional<Follower> followerOptional = followerRepo.findByChatId(updateDto.getChatId());

        if (followerOptional.isPresent()) {
            Follower follower = followerOptional.get();
            follower.setFirstName(updateDto.getFirstname());
            follower.setLastName(updateDto.getLastname());
            follower.setUsername(updateDto.getUsername());
            follower.setLastConnectionDate((LocalDateTime.now().toInstant(ZoneOffset.UTC)).getEpochSecond());
            followerRepo.save(follower);
        } else {
            addNewFollower(updateDto);
        }
    }

    public Follower addNewFollower(UpdateDto updateDto) {

        Long startedDate = (LocalDateTime.now().toInstant(ZoneOffset.UTC)).getEpochSecond();

        Follower follower = new Follower();
        follower.setFirstName(updateDto.getFirstname());
        follower.setLastName(updateDto.getLastname());
        follower.setUsername(updateDto.getUsername());
        follower.setChatId(updateDto.getChatId());
        follower.setStartedDate(startedDate);
        follower.setLastConnectionDate(startedDate);
        return followerRepo.save(follower);
    }

    public TimeDto getBetweenTwoTime(LocalDateTime fromDateTime, LocalDateTime toDateTime, String zoneId) {

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        TimeDto timeDto = new TimeDto();

        timeDto.setZoneId(zoneId == null ? "UTC" : zoneId);


        timeDto.setYears((int) tempDateTime.until(toDateTime, ChronoUnit.YEARS));
        tempDateTime = tempDateTime.plusYears(timeDto.getYears());

        timeDto.setMonths((int) tempDateTime.until(toDateTime, ChronoUnit.MONTHS));
        tempDateTime = tempDateTime.plusMonths(timeDto.getMonths());

        timeDto.setDays((int) tempDateTime.until(toDateTime, ChronoUnit.DAYS));
        tempDateTime = tempDateTime.plusDays(timeDto.getDays());

        timeDto.setHours((int) tempDateTime.until(toDateTime, ChronoUnit.HOURS));
        tempDateTime = tempDateTime.plusHours(timeDto.getHours());

        timeDto.setMinutes((int) tempDateTime.until(toDateTime, ChronoUnit.MINUTES));
        tempDateTime = tempDateTime.plusMinutes(timeDto.getMinutes());

        timeDto.setSeconds((int) tempDateTime.until(toDateTime, ChronoUnit.SECONDS));


        return timeDto;
    }
}
