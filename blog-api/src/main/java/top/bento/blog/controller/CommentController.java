package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.bento.blog.service.CommentService;
import top.bento.blog.vo.Result;
import top.bento.blog.vo.params.CommentParam;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result GetComments(@PathVariable("id") Long id) {
        return Result.success(commentService.getCommentsByArticleId(id));
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        commentService.insertComment(commentParam);
        return Result.success(null);
    }
}
