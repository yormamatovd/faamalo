package org.example.faamalobot.repo;

import org.example.faamalobot.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository
public interface FollowerRepo extends JpaRepository<Follower, Long> {
    Optional<Follower> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);


    @Query(value = "select chat_id from follower",
            nativeQuery = true)
    HashSet<Long> getAllChatId();

    @Query(value = "select chat_id from follower limit :limit",
            nativeQuery = true)
    HashSet<Long> getFollowersLimit(long limit);
}
