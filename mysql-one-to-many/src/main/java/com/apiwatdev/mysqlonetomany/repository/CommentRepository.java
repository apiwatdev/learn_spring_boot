package com.apiwatdev.mysqlonetomany.repository;

import com.apiwatdev.mysqlonetomany.model.Comment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTutorialId(Long Id);

    @Transactional
    void deleteByTutorialId(long tutorialId);


}
