package org.example.faamalobot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto {
    private Long index;

    private String fileId;

    private String photoUrl;

    private Long nextImgId;
    private Long previewImgId;

    private Boolean hasNext;
    private Boolean hasPreview;

    private Integer total;
}
