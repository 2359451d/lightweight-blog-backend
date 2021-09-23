package top.bento.blog.service;

import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.vo.SysUserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUserByCredentials(String account, String password);

    SysUser findUserByAccount(String account);

    SysUserVo findUserByToken(String token);

    void save(SysUser user);
}
