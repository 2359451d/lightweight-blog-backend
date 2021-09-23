package top.bento.blog.service;

import org.springframework.transaction.annotation.Transactional;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.vo.params.LoginParams;
import top.bento.blog.vo.params.RegisterParams;

@Transactional
public interface AuthenticationService {
    String login(LoginParams loginParams);

    String logout(String token);

    String register(RegisterParams registerParams);

    SysUser validateToken(String token);
}
