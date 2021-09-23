package top.bento.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.bento.blog.dao.pojo.Tag;
import top.bento.blog.vo.TagVo;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * list tags by article id
     *
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * list all the tags according to the id set
     * @param ids
     * @return
     */
    List<Tag> findTagsByIds(List<Long> ids);

    /**
     * find the most popular tag ids by the associated article counts
     * group by tag_id, counts the articles then sort and pick top limit(int)
     * @param limit
     * @return
     */
    List<Long> findHotTagIds(int limit);

}
