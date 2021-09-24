package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bento.blog.dao.dos.Archives;
import top.bento.blog.dao.mapper.ArticleBodyMapper;
import top.bento.blog.dao.mapper.ArticleMapper;
import top.bento.blog.dao.pojo.Article;
import top.bento.blog.dao.pojo.ArticleBody;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.service.*;
import top.bento.blog.vo.ArticleBodyVo;
import top.bento.blog.vo.ArticleVo;
import top.bento.blog.vo.TagVo;
import top.bento.blog.vo.params.PageParams;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ThreadService threadService;

    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        ArticleVo copy = copy(article, true, true, true, true);
        // update the view count => processed by the thread pool
        threadService.updateArticleViewCount(articleMapper, article);
        return copy;
    }

    @Override
    public List<Archives> listArchives() {
        return articleMapper.listArchives();
    }

    @Override
    public List<ArticleVo> listNewestArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return copyList(articles, false, false);
    }

    @Override
    public List<ArticleVo> listTopHottestArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return copyList(articles, false, false);
    }

    @Override
    public List<ArticleVo> listArticles(PageParams pageParams) {
        // pagination query, list newest articles by created date
        // & weight (whether pinned)
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        //LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        // convert pojo list to vo list
        // tag & author info needed

        return copyList(records, true, true);
    }

    /**
     * convert Article collection to the ArticleVo collection
     *
     * @param records
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> list = new ArrayList<>();
        records.forEach(article -> {
            list.add(copy(article, isTag, isAuthor, false, false));
        });
        return list;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> list = new ArrayList<>();
        records.forEach(article -> {
            list.add(copy(article, isTag, isAuthor, isBody, isCategory));
        });
        return list;
    }


    /**
     * convert single Article to the ArticleVo object
     * if tag & author info are needed, then provide
     *
     * @param article
     * @return
     */
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        // convert id Long(pojo) -> String(vo)
        articleVo.setId(String.valueOf(article.getId()));
        // convert create date Long(pojo) -> String(vo)
        // if null, set the latest time
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("dd/MM/yyyy HH:mm"));

        if (isTag) {
            // list tags by article id
            List<TagVo> tags = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tags);
        }
        if (isAuthor) {
            SysUser user = sysUserService.findUserById(article.getAuthorId());
            articleVo.setAuthor(user.getNickname());
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    /**
     * find article body by body id
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
