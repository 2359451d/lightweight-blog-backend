package top.bento.blog.service;

import top.bento.blog.vo.CommentVo;
import top.bento.blog.vo.params.CommentParam;

import java.util.List;

public interface CommentService {
    List<CommentVo> getCommentsByArticleId(Long id);

    void insertComment(CommentParam commentParam);
}
