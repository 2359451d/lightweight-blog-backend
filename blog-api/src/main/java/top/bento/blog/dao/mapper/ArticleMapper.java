package top.bento.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.bento.blog.dao.dos.Archives;
import top.bento.blog.dao.pojo.Article;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * list article archives
     * @return
     */
    List<Archives> listArchives();
}
