package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.bento.blog.service.AuthenticationService;
import top.bento.blog.vo.Result;
import top.bento.blog.vo.params.LoginParams;
import top.bento.blog.vo.params.RegisterParams;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    public Result login(@RequestBody LoginParams loginParams) {
        String token = null;
        token = authenticationService.login(loginParams);
        return Result.success(token);
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        return Result.success(authenticationService.logout(token));
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterParams registerParams) {
        return Result.success(authenticationService.register(registerParams));
    }
}
