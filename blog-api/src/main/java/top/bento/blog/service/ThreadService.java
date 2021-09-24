package top.bento.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.bento.blog.dao.mapper.ArticleMapper;
import top.bento.blog.dao.pojo.Article;

@Component
public class ThreadService {

    // process by thread pool, which won't affect the main thread
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        // optimistic lock - CAS, check if not changed before current update
        queryWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleUpdate,queryWrapper);
    }
}
