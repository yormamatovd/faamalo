package org.example.faamalobot.repo;

import org.example.faamalobot.entity.NameBase;
import org.example.faamalobot.enums.WaitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NameBaseRepo extends JpaRepository<NameBase, Long> {

    boolean existsByNameAndType(String name, WaitType type);

    boolean existsByNameAndTypeAndTemplateFileId(String name, WaitType type, String templateFileId);

    List<NameBase> findByNameAndType(String name, WaitType type);
}
