package org.example.faamalobot.service;

import org.example.faamalobot.entity.MyFont;
import org.example.faamalobot.model.TemplateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ImageService {

    @Value(value = "${fonts_path}")
    private String fontBaseFolder;

    public BufferedImage margeImage(int y, boolean customY, int x, boolean customX, BufferedImage background, BufferedImage insertable) {

        if (!customY) y = (background.getHeight() / 100) * y;
        if (!customX) x = (int) (Float.parseFloat(background.getWidth() + "") / 100 * x) - (insertable.getWidth() / 2);

        Graphics2D g = background.createGraphics();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g.drawImage(insertable, x, y, null);

        g.dispose();
        return background;
    }

    public BufferedImage getText(int width, float letterSpacing, String text, Font font, Color color) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        int height = fm.getHeight() * 7;
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, letterSpacing);
        font = font.deriveFont(attributes);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(color);
        g2d.drawString(text, 50, (height / 100) * 75);
        g2d.dispose();
        return crop(img);
    }

    public Font getFont(String pathTTf, float size, int fontType) {
        Font font = null;
        try {
            InputStream fontInputStream = getFileFromResourceAsStream(pathTTf);
            font  = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontInputStream));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        assert font != null;
        return font.deriveFont(fontType, size);
    }

    private BufferedImage crop(BufferedImage image) {
        int minY = 0, maxY = 0, minX = Integer.MAX_VALUE, maxX = 0;
        boolean isBlank, minYIsDefined = false;
        Raster raster = image.getRaster();

        for (int y = 0; y < image.getHeight(); y++) {
            isBlank = true;

            for (int x = 0; x < image.getWidth(); x++) {
                //Change condition to (raster.getSample(x, y, 3) != 0)
                //for better performance
                if (raster.getPixel(x, y, (int[]) null)[3] != 0) {
                    isBlank = false;

                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                }
            }

            if (!isBlank) {
                if (!minYIsDefined) {
                    minY = y;
                    minYIsDefined = true;
                } else {
                    if (y > maxY) maxY = y;
                }
            }
        }

        return image.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    public BufferedImage templateN1(String text, MyFont myFont, BufferedImage tempBuffImg, TemplateDto templateDto) {
        BufferedImage insertable;
        Color color1 = new Color(templateDto.getColorR(), templateDto.getColorG(), templateDto.getColorB());

        if (tempBuffImg != null) {
            if (templateDto.getBorder() != null) {
                insertable = getText(tempBuffImg.getWidth(), templateDto.getLetterSpacing(), text, getFont(fontBaseFolder + myFont.getFontPath(), templateDto.getFontSize(), Font.TRUETYPE_FONT), new Color(0, 0, 0, 55));

                if (templateDto.getBorder().equals("t-r")) {
                    tempBuffImg = margeImage(templateDto.getY() - 1, templateDto.getCustomY(), templateDto.getX() + 1, templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("t-l")) {
                    tempBuffImg = margeImage(templateDto.getY() - 1, templateDto.getCustomY(), templateDto.getX() - 1, templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("b-r")) {
                    tempBuffImg = margeImage(templateDto.getY() + 1, templateDto.getCustomY(), templateDto.getX() + 1, templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("b-l")) {
                    tempBuffImg = margeImage(templateDto.getY() + 1, templateDto.getCustomY(), templateDto.getX() - 1, templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("t")) {
                    tempBuffImg = margeImage(templateDto.getY() - 1, templateDto.getCustomY(), templateDto.getX(), templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("r")) {
                    tempBuffImg = margeImage(templateDto.getY(), templateDto.getCustomY(), templateDto.getX() + 1, templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("b")) {
                    tempBuffImg = margeImage(templateDto.getY() + 1, templateDto.getCustomY(), templateDto.getX(), templateDto.getCustomX(), tempBuffImg, insertable);
                } else if (templateDto.getBorder().equals("l")) {
                    tempBuffImg = margeImage(templateDto.getY(), templateDto.getCustomY(), templateDto.getX() - 1, templateDto.getCustomX(), tempBuffImg, insertable);
                }
            }
            insertable = getText(tempBuffImg.getWidth(), templateDto.getLetterSpacing(), text, getFont(fontBaseFolder + myFont.getFontPath(), templateDto.getFontSize(), Font.TRUETYPE_FONT), color1);

            return margeImage(templateDto.getY(), templateDto.getCustomY(), templateDto.getX(), templateDto.getCustomX(), tempBuffImg, insertable);
        }

        return null;
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

}
