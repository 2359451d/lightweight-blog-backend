package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.bento.blog.dao.pojo.Article;
import top.bento.blog.service.ArticleService;
import top.bento.blog.vo.ArticleVo;
import top.bento.blog.vo.params.ArticleParam;
import top.bento.blog.vo.params.PageParams;
import top.bento.blog.vo.Result;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * homepage list articles
     *
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        List<ArticleVo> articles = articleService.listArticles(pageParams);
        return Result.success(articles);
    }

    /**
     * list the top hottest articles
     *
     * @return
     */
    @PostMapping("hot")
    public Result listTopHottestArticles() {
        int limit = 5;
        List<ArticleVo> topHottestArticle = articleService.listTopHottestArticles(limit);
        return Result.success(topHottestArticle);
    }

    /**
     * list the top newest articles
     *
     * @return
     */
    @PostMapping("new")
    public Result listNewestArticles() {
        int limit = 5;
        List<ArticleVo> newestArticles = articleService.listNewestArticles(limit);
        return Result.success(newestArticles);
    }

    @PostMapping("listArchives")
    public Result listArchives() {
        return Result.success(articleService.listArchives());
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        Map<String, String> map = articleService.publish(articleParam);
        return Result.success(map);
    }
}
