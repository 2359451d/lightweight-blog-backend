package top.bento.blog.vo;

import lombok.Data;

@Data
public class SysUserVo {

    private Long id;

    private String account;

    private String nickname;

    private String avatar;
}
