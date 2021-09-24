package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bento.blog.dao.mapper.CommentMapper;
import top.bento.blog.dao.pojo.Comment;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.service.CommentService;
import top.bento.blog.service.SysUserService;
import top.bento.blog.utils.UserThreadLocal;
import top.bento.blog.vo.CommentVo;
import top.bento.blog.vo.CommenterVo;
import top.bento.blog.vo.params.CommentParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void insertComment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentMapper.insert(comment);
    }

    /**
     * fetch all the comments by article id
     * 1. retrieve all the comments by article id
     * following operations are done in the method 'copy()'
     * 2. fetch corresponding commenter information
     * 3. if comment level==1, exists nested comments list,
     * fetch all the sub-comments by parent_id
     *
     * @param id
     * @return
     */
    @Override
    public List<CommentVo> getCommentsByArticleId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<Comment>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        // serialize pojo to vo
        List<CommentVo> commentVoList = copyList(comments);
        return commentVoList;
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);

        // commenter information
        Long authorId = comment.getAuthorId();
        CommenterVo commenterVo = sysUserService.findCommenterById(authorId);
        commentVo.setAuthor(commenterVo);
        // sub-comments
        Integer level = comment.getLevel();
        if (level == 1) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        // article author info
        if (level > 1) {
            Long toUid = comment.getToUid();
            CommenterVo toUser = sysUserService.findCommenterById(toUid);
            commentVo.setToUser(toUser);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
