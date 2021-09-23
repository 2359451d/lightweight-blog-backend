package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.bento.blog.dao.mapper.TagMapper;
import top.bento.blog.dao.pojo.Tag;
import top.bento.blog.service.TagService;
import top.bento.blog.vo.TagVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * find the most popular tags by the associated article counts
     * group by tag_id, counts the articles then sort and pick top limit(int)
     * @param limit
     * @return
     */
    public List<TagVo> findHotTagIds(int limit) {
        List<Long> hotTagIds = tagMapper.findHotTagIds(limit);
        if (CollectionUtils.isEmpty(hotTagIds)) {
            return new ArrayList<TagVo>();
        }
        // list all the tags in the Id set
        List<Tag> tags = tagMapper.findTagsByIds(hotTagIds);

        return copyList(tags);
    }

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // MB donot support query for multiple tables
        // using custom mapper method
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    private List<TagVo> copyList(List<Tag> tags) {
        ArrayList<TagVo> tagVos = new ArrayList<>();
        tags.forEach(tag -> {
            tagVos.add(copy(tag));
        });
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }


}
