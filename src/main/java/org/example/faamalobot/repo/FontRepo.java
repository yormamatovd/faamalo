package org.example.faamalobot.repo;

import org.example.faamalobot.entity.MyFont;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FontRepo extends JpaRepository<MyFont, Long> {

    MyFont findByFontCode(String fontCode);
}
