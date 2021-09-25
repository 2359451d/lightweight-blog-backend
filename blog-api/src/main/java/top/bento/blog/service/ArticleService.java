package top.bento.blog.service;

import top.bento.blog.dao.dos.Archives;
import top.bento.blog.vo.ArticleVo;
import top.bento.blog.vo.params.ArticleParam;
import top.bento.blog.vo.params.PageParams;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    /**
     * pagination query for article list
     * @param pageParams
     * @return
     */
    List<ArticleVo> listArticles(PageParams pageParams);

    /**
     * list the top hottest articles
     * @return
     */
    List<ArticleVo> listTopHottestArticles(int limit);

    /**
     * list the top newest articles
     * @return
     */
    List<ArticleVo> listNewestArticles(int limit);

    /**
     * list article archives
     * @return
     */
    List<Archives> listArchives();

    ArticleVo findArticleById(Long id);

    Map<String, String> publish(ArticleParam articleParam);
}
