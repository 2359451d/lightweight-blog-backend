package top.bento.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

    private String id;

    private String title;

    private String summary;

    private int commentCounts;

    private int viewCounts;

    private int weight;
    /**
     * createDate: db(Long) -> vo(String)
     */
    private String createDate;

    /**
     * authorId: db(Long) -> vo(String)
     */
    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}