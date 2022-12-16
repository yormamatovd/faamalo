package org.example.faamalobot.common;

import org.example.faamalobot.entity.MyFont;
import org.example.faamalobot.entity.Template;
import org.example.faamalobot.enums.WaitType;
import org.example.faamalobot.repo.FontRepo;
import org.example.faamalobot.repo.TemplateRepo;
import org.example.faamalobot.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    TemplateRepo templateRepo;
    @Autowired
    FontRepo fontRepo;
    @Autowired
    MessageSender sender;

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) {
        if (initialMode.equals("always")) {

            List<MyFont> fontList = new ArrayList<>();
            fontList.add(new MyFont("bebas-reg", "BebasNeue-Regular.ttf"));
            fontList.add(new MyFont("myraid-pro-reg", "MyriadPro-Regular.ttf"));
            fontList.add(new MyFont("pfd-bold", "PlayfairDisplay-Bold.ttf"));
            fontList.add(new MyFont("ram-kar", "Ramadhan-Karim.ttf"));
            fontList.add(new MyFont("hid-demo", "HidayatullahDemo-mLp39.ttf"));
            fontList.add(new MyFont("amsi-light", "AmsiProNormal-ExtraLight.ttf"));
            fontList.add(new MyFont("perf-christ", "ThePerfectChristmas.ttf"));
            fontList.add(new MyFont("wav-christ", "WavingAtChristmas.ttf"));
            fontList.add(new MyFont("welc-christ", "WelcomeChristmas.ttf"));
            fontList.add(new MyFont("ds-arabic", "dsArabic.ttf"));
            fontList.add(new MyFont("aid-fitri", "Aidilfitri-1Gaa2.ttf"));
            fontList.add(new MyFont("comic", "comic.ttf"));
            fontList.add(new MyFont("georgia", "Georgia.ttf"));
            fontList.add(new MyFont("patrick", "PatrickHand-Regular.ttf"));
            fontList.add(new MyFont("constan", "constan.ttf"));
            fontList.add(new MyFont("comic-sans-ms", "ComicSansMS.ttf"));
            fontList.add(new MyFont("gabriola", "gabriola.ttf"));

            fontRepo.saveAll(fontList);

            List<Template> templates = new ArrayList<>();

            //todo new year templates
            templates.add(new Template(
                    "newYear/template1.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME, WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME),
                    false, 51, false, 67,
                    "pfd-bold",
                    38, 24, 15, 100,
                    121,
                    .5f,
                    null,
                    "new-year-temp-1"
            ));
            templates.add(new Template(
                    "newYear/template2.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME, WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME),
                    false, 50, false, 77,
                    "bebas-reg",
                    235, 235, 222, 100,
                    150,
                    .5f,
                    null,
                    "new-year-temp-2"
            ));
            templates.add(new Template(
                    "newYear/template3.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME, WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME),
                    false, 50, false, 45,
                    "bebas-reg",
                    52, 52, 52, 100,
                    250,
                    .1f,
                    null,
                    "new-year-temp-3"
            ));
            templates.add(new Template(
                    "newYear/template4.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME),
                    false, 50, false, 22,
                    "welc-christ",
                    255, 255, 255, 100,
                    600,
                    .1f,
                    null,
                    "new-year-temp-4"
            ));
            templates.add(new Template(
                    "newYear/template5.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, false, 48,
                    "welc-christ",
                    255, 255, 255, 100,
                    228,
                    .0f,
                    null,
                    "new-year-temp-5"
            ));
            templates.add(new Template(
                    "newYear/template6.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 48, false, 57,
                    "perf-christ",
                    255, 255, 255, 100,
                    350,
                    .0f,
                    null,
                    "new-year-temp-6"
            ));
            templates.add(new Template(
                    "newYear/template7.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, true, 880,
                    "constan",
                    235, 42, 40, 100,
                    60,
                    .0f,
                    null,
                    "new-year-temp-7"
            ));
            templates.add(new Template(
                    "newYear/template8.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, true, 820,
                    "constan",
                    243,242,225, 100,
                    120,
                    .1f,
                    null,
                    "new-year-temp-8"
            ));
            templates.add(new Template(
                    "newYear/template9.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, false, 56,
                    "constan",
                    242,237,233, 100,
                    100,
                    .1f,
                    null,
                    "new-year-temp-9"
            ));
            templates.add(new Template(
                    "newYear/template10.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, false, 50,
                    "comic-sans-ms",
                    22,25,24, 100,
                    120,
                    .1f,
                    null,
                    "new-year-temp-10"
            ));
            templates.add(new Template(
                    "newYear/template11.jpg",
                    List.of(WaitType.WAIT_FOR_NEW_YEAR_BOY_NAME, WaitType.WAIT_FOR_NEW_YEAR_GIRL_NAME),
                    false, 50, false, 53,
                    "constan",
                    0,0,0, 100,
                    200,
                    .1f,
                    null,
                    "new-year-temp-11"
            ));

            //todo templates juma
            templates.add(new Template(
                    "juma/template1.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_GROUP_NAME),
                    true, 2600, false, 63,
                    "myraid-pro-reg",
                    131, 100, 54, 100,
                    50,
                    .2f,
                    null,
                    "juma-temp-1"
            ));
            templates.add(new Template(
                    "juma/template2.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME, WaitType.WAIT_FOR_JUMA_GIRL_NAME, WaitType.WAIT_FOR_JUMA_GROUP_NAME),
                    false, 50, false, 75,
                    "hid-demo",
                    186, 115, 25, 100,
                    166,
                    .1f,
                    null,
                    "juma-temp-2"
            ));
            templates.add(new Template(
                    "juma/template3.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME, WaitType.WAIT_FOR_JUMA_GIRL_NAME),
                    false, 45, false, 63,
                    "hid-demo",
                    217, 178, 98, 100,
                    160,
                    .3f,
                    null,
                    "juma-temp-3"
            ));
            templates.add(new Template(
                    "juma/template4.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME, WaitType.WAIT_FOR_JUMA_GIRL_NAME),
                    false, 50, false, 66,
                    "hid-demo",
                    255, 255, 255, 100,
                    90,
                    .2f,
                    null,
                    "juma-temp-4"
            ));
            templates.add(new Template(
                    "juma/template5.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME, WaitType.WAIT_FOR_JUMA_GIRL_NAME, WaitType.WAIT_FOR_JUMA_GROUP_NAME),
                    false, 50, false, 63,
                    "amsi-light",
                    131, 131, 131, 100,
                    80,
                    .2f,
                    null,
                    "juma-temp-5"
            ));
            templates.add(new Template(
                    "juma/template6.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME, WaitType.WAIT_FOR_JUMA_GIRL_NAME),
                    false, 50, false, 53,
                    "ds-arabic",
                    243, 237, 157, 100,
                    90,
                    .1f,
                    null,
                    "juma-temp-6"
            ));
            templates.add(new Template(
                    "juma/template7.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME),
                    false, 50, false, 91,
                    "aid-fitri",
                    196, 170, 105, 100,
                    58,
                    .2f,
                    null,
                    "juma-temp-7"
            ));
            templates.add(new Template(
                    "juma/template8.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_GIRL_NAME),
                    false, 50, false, 80,
                    "ram-kar",
                    0, 32, 53, 100,
                    120,
                    .2f,
                    null,
                    "juma-temp-8"
            ));
            templates.add(new Template(
                    "juma/template9.jpg",
                    List.of(WaitType.WAIT_FOR_JUMA_BOY_NAME),
                    false, 50, false, 80,
                    "ram-kar",
                    0, 32, 53, 100,
                    120,
                    .2f,
                    null,
                    "juma-temp-9"
            ));

            //todo birthday templates
            templates.add(new Template(
                    "birthday/template1.jpg",
                    List.of(WaitType.WAIT_FOR_BIRTH_BOY_NAME, WaitType.WAIT_FOR_BIRTH_GIRL_NAME),
                    false, 50, false, 48,
                    "comic",
                    205, 171, 70, 100,
                    210,
                    .1f,
                    null,
                    "birth-temp-1"
            ));
            templates.add(new Template(
                    "birthday/template2.jpg",
                    List.of(WaitType.WAIT_FOR_BIRTH_BOY_NAME, WaitType.WAIT_FOR_BIRTH_GIRL_NAME),
                    false, 50, false, 19,
                    "georgia",
                    236, 145, 74, 100,
                    250,
                    .1f,
                    null,
                    "birth-temp-2"
            ));
            templates.add(new Template(
                    "birthday/template3.jpg",
                    List.of(WaitType.WAIT_FOR_BIRTH_BOY_NAME, WaitType.WAIT_FOR_BIRTH_GIRL_NAME),
                    true, 70, false, 36,
                    "patrick",
                    191, 0, 61, 100,
                    120,
                    .1f,
                    "b",
                    "birth-temp-3"
            ));
            templates.add(new Template(
                    "birthday/template4.jpg",
                    List.of(WaitType.WAIT_FOR_BIRTH_BOY_NAME, WaitType.WAIT_FOR_BIRTH_GIRL_NAME),
                    false, 51, false, 29,
                    "georgia",
                    204, 154, 69, 100,
                    155,
                    .1f,
                    null,
                    "birth-temp-4"
            ));

            templateRepo.saveAll(templates);

        }
    }
}
