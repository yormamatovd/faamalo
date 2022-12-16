package org.example.faamalobot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Yormamatov Davronbek
 * @since 30.01.2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsyncBoardDto {

    private boolean sendByDesc;
    private boolean notification;
    private long sendFollowerCount;
    private long allFollowerCount;
    private String channelUsername;
    private Integer postId;
}
