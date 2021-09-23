package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.bento.blog.service.SysUserService;
import top.bento.blog.vo.Result;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result getUerAuthorizationInfo(@RequestHeader("Authorization") String token) {
        return Result.success(sysUserService.findUserByToken(token));
    }
}
