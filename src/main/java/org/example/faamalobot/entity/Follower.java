package org.example.faamalobot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.faamalobot.enums.WaitType;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long chatId;

    private String firstName;

    private String LastName;

    private String username;

    private Long startedDate;

    private Long lastConnectionDate;

    @Enumerated(EnumType.STRING)
    private WaitType waitType;

    private String firstMessageId;

    private String timeZoneId;

}
