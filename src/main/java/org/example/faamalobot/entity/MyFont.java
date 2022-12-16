package org.example.faamalobot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyFont {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String fontCode;

    private String fontPath;

    public MyFont(String fontCode, String fontPath) {
        this.fontCode = fontCode;
        this.fontPath = fontPath;
    }
}
