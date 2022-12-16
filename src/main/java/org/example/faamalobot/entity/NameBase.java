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
public class NameBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String fileId;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String templateFileId;

    @Enumerated(EnumType.STRING)
    private WaitType type;

    public NameBase(String fileId, String name, String templateFileId, WaitType type) {
        this.fileId = fileId;
        this.name = name;
        this.templateFileId = templateFileId;
        this.type = type;
    }
}
