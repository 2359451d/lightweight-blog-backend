package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bento.blog.dao.dos.Archives;
import top.bento.blog.dao.mapper.ArticleBodyMapper;
import top.bento.blog.dao.mapper.ArticleMapper;
import top.bento.blog.dao.mapper.ArticleTagMapper;
import top.bento.blog.dao.pojo.Article;
import top.bento.blog.dao.pojo.ArticleBody;
import top.bento.blog.dao.pojo.ArticleTag;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.service.*;
import top.bento.blog.utils.UserThreadLocal;
import top.bento.blog.vo.ArticleBodyVo;
import top.bento.blog.vo.ArticleVo;
import top.bento.blog.vo.TagVo;
import top.bento.blog.vo.params.ArticleParam;
import top.bento.blog.vo.params.PageParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

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

    /**
     * using MB custom sql mapper, avoid single checking branch to set query wrapper
     * @param pageParams
     * @return
     */
    @Override
    public List<ArticleVo> listArticles(PageParams pageParams) {
        // pagination query, list newest articles by created date
        // & weight (whether pinned)
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return copyList(records, true, true);
    }

    @Deprecated
    public List<ArticleVo> listArticlesDeprecated(PageParams pageParams) {
        // pagination query, list newest articles by created date
        // & weight (whether pinned)
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        //LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        // article pagination in category/{id}
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
        }

        if (pageParams.getTagId() != null) {
            List<Long> articleIdList = new ArrayList<>();
            LambdaQueryWrapper<ArticleTag> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0) {
                // select in set
                queryWrapper.in(Article::getId, articleIdList);
            }
        }

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
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    /**
     * find article body by body id
     *
     * @param bodyId
     * @return
     */
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    /**
     * post a new article
     *
     * @param articleParam
     * @return
     */
    @Override
    public Map<String, String> publish(ArticleParam articleParam) {
        Article article = new Article();
        // author info
        SysUser user = UserThreadLocal.get();
        article.setAuthorId(user.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        articleMapper.insert(article); // article id would be generated after insertion

        // tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                Long articleId = article.getId();
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }

        // body (content, contentHtml)
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        // set body id
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return map;
    }
}
