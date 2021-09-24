package top.bento.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {

    // avoid precision loss of frontend
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private CommenterVo author; // commenter

    private String content;

    private List<CommentVo> childrens;//sub-comments

    private String createDate;

    private Integer level;//comments nested level

    private CommenterVo toUser; // article author
}
