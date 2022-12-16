package org.example.faamalobot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.faamalobot.enums.WaitType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;

    @Column(nullable = false)
    @ElementCollection(targetClass = WaitType.class)
    @Enumerated(EnumType.STRING)
    private List<WaitType> waitTypes;

    private Integer x;
    private Integer y;
    private Boolean customX;
    private Boolean customY;

    private String fontCode;

    private Integer colorR;
    private Integer colorG;
    private Integer colorB;
    private Integer colorA;

    private Integer fontSize;

    private Float letterSpacing;

    private String border;

    private String customName;

    public Template(String fileId, List<WaitType> waitTypes, Boolean customX, Integer x, Boolean customY, Integer y, String fontCode, Integer colorR, Integer colorG, Integer colorB, Integer colorA, Integer fontSize, Float letterSpacing, String border, String customName) {
        this.fileId = fileId;
        this.waitTypes = waitTypes;
        this.x = x;
        this.y = y;

        this.customX = customX;
        this.customY = customY;
        this.fontCode = fontCode;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.colorA = colorA;
        this.fontSize = fontSize;
        this.letterSpacing = letterSpacing;
        this.border = border;
        this.customName = customName;
    }
}
