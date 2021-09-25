package top.bento.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Page;
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

    IPage<Article> listArticle(IPage<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
}
