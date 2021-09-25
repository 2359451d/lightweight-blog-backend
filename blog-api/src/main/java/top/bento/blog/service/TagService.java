package top.bento.blog.service;

import top.bento.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    List<TagVo> findHotTagIds(int limit);

    List<TagVo> listAllTags();

    List<TagVo> listAllTagDetails();

    TagVo getTagDetailById(Long id);
}
