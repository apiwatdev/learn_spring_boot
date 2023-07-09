package com.apiwatdev.mysqlonetomany.controller;

import com.apiwatdev.mysqlonetomany.exception.ResourceNotFoundException;
import com.apiwatdev.mysqlonetomany.model.Comment;
import com.apiwatdev.mysqlonetomany.repository.CommentRepository;
import com.apiwatdev.mysqlonetomany.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://locahost:8081")
@RestController
@RequestMapping(path = "/api")
public class CommentController {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping(path = "/tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByTutorial(@PathVariable(value = "tutorialId") Long tutorialId){
        if(!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        }

        List<Comment> comments = commentRepository.findByTutorialId(tutorialId);
        return new ResponseEntity<>(comments, HttpStatus.OK);

    }

    @GetMapping(path = "/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable(value = "id") Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));
        return new ResponseEntity<>(comment, HttpStatus.OK);

    }
    @PostMapping(path = "/tutorials/{tutorialId}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable(value = "tutorialId") Long tutorialId,
            @RequestBody Comment commentRequest) {

        Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
            commentRequest.setTutorial(tutorial);
            return commentRepository.save(commentRequest);
        }).orElseThrow(()-> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping(path = "/comments/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable(value = "id") Long  id,
            @RequestBody Comment commentRequest) {
        Comment _comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));

        _comment.setContent(commentRequest.getContent());

        return new ResponseEntity<>(commentRepository.save(_comment), HttpStatus.OK);
    }

    @DeleteMapping(path = "/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable(value = "id") long  id) {
        commentRepository.deleteById(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/tutorials/{tutorialId}/comments")
    public ResponseEntity<HttpStatus> deleteAllCommentOfTutorial(@PathVariable(value = "tutorialId") Long tutorialId) {

        if(!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        }
        commentRepository.deleteByTutorialId(tutorialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
