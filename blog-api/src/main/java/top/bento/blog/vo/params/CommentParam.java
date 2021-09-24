package top.bento.blog.vo.params;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentParam {

    private Long articleId;

    private String content;

    // avoid precision loss of frontend
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parent;

    private Long toUserId;
}
