package top.bento.blog.service;

import org.springframework.stereotype.Component;
import top.bento.blog.dao.mapper.ArticleMapper;
import top.bento.blog.dao.pojo.Article;

@Component
public class ThreadService {
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
    }
}
