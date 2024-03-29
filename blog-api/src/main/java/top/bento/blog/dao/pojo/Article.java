package top.bento.blog.dao.pojo;

import lombok.Data;

@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * author id
     */
    private Long authorId;
    /**
     *
     * body id
     */
    private Long bodyId;
    /**
     * category id
     */
    private Long categoryId;

    /**
     * whether pin at the top
     */
    private Integer weight;


    /**
     * create date
     */
    private Long createDate;
}
