package org.example.faamalobot.model;

public interface TemplateDto {

    Long getId();

    String getFileId();

    Integer getX();

    Integer getY();

    Boolean getCustomX();
    Boolean getCustomY();

    String getFontCode();

    Integer getColorR();

    Integer getColorG();

    Integer getColorB();

    Integer getColorA();

    Integer getFontSize();

    Float getLetterSpacing();

    String getBorder();

    String getCustomName();
}
