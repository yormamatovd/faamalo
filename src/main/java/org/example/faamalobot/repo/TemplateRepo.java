package org.example.faamalobot.repo;

import org.example.faamalobot.entity.Template;
import org.example.faamalobot.model.TemplateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {

    @Query(
            value =
                    "select distinct t.id             as id,\n" +
                            "                t.file_id        as fileId,\n" +
                            "                t.colorr         as colorR,\n" +
                            "                t.colorg         as colorG,\n" +
                            "                t.colorb         as colorB,\n" +
                            "                t.colora         as colorA,\n" +
                            "                t.font_size      as fontSize,\n" +
                            "                t.letter_spacing as letterSpacing,\n" +
                            "                t.border as border,\n" +
                            "                t.x              as X,\n" +
                            "                t.y              as Y,\n" +
                            "                t.customx              as customX,\n" +
                            "                t.customy              as customY,\n" +
                            "                t.font_code      as fontCode\n" +
                            "\n" +
                            "from template t\n" +
                            "         left join template_wait_types twt on t.id = twt.template_id\n" +
                            "where twt.wait_types IN (:waitType)",
            nativeQuery = true
    )
    List<TemplateDto> findAllByWaitTypes(String waitType);
}
