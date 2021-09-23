package top.bento.blog.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.exception.AuthenticationException;
import top.bento.blog.exception.TokenValidationException;
import top.bento.blog.service.AuthenticationService;
import top.bento.blog.service.SysUserService;
import top.bento.blog.utils.JWTUtils;
import top.bento.blog.vo.ErrorCode;
import top.bento.blog.vo.params.LoginParams;
import top.bento.blog.vo.params.RegisterParams;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Authentication centre
 */
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "random!@#";

    /**
     * register new sys user
     * 1. check whether given params are valid
     * 2. check whether user exists
     * 3. generate new token & return
     * 4. store into redis
     * !NOTE: if any exceptions, rollback
     * @param registerParams
     * @return
     */
    @Override
    public String register(RegisterParams registerParams) {
        String account = registerParams.getAccount();
        String password = registerParams.getPassword();
        String nickname = registerParams.getNickname();

        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            throw new AuthenticationException(ErrorCode.INVALID_PARAMS_ERROR.getCode(), ErrorCode.INVALID_PARAMS_ERROR.getMsg());
        }

        SysUser user = sysUserService.findUserByAccount(account);
        if (user != null) {
            throw new AuthenticationException(ErrorCode.ACCOUNT_ALREADY_EXIST.getCode(), ErrorCode.ACCOUNT_ALREADY_EXIST.getMsg());
        }

        // fill the default field
        user = new SysUser();
        user.setNickname(nickname);
        user.setAccount(account);
        user.setPassword(DigestUtils.md5Hex(password+salt));
        user.setCreateDate(System.currentTimeMillis());
        user.setLastLogin(System.currentTimeMillis());
        user.setAvatar("/static/img/logo.b3a48c0.png");
        user.setAdmin(1); // 1:true
        user.setDeleted(0); // 0:false
        user.setSalt("");
        user.setStatus("");
        user.setEmail("");
        sysUserService.save(user); // transaction
        //token
        String token = JWTUtils.createToken(user.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return token;
    }

    @Override
    public String logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return "Logout successfully";
    }

    /**
     * Generate JWT token for given user credentials
     * @param loginParams
     * @return
     */
    @Override
    public String login(LoginParams loginParams) {
        /*
         * check whether given credential is valid
         * check whether user exists
         * if exist, using JWT generating token
         * store token in redis, <k,v>: token: userinfo set_expiration_period
         * */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        password = DigestUtils.md5Hex(password + salt);

        // param error
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new AuthenticationException(ErrorCode.INVALID_PARAMS_ERROR.getCode(), ErrorCode.INVALID_PARAMS_ERROR.getMsg());
        }

        // user not exist
        SysUser user = sysUserService.findUserByCredentials(account, password);
        if (user == null) {
            throw new AuthenticationException(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        // generate JWT token
        String token = JWTUtils.createToken(user.getId());
        System.out.println("TOOOOOOOOOOKEN==========="+token);

        // set redis kv
        redisTemplate.opsForValue().set("TOKEN_" + token,
                JSON.toJSONString(user),
                1, TimeUnit.DAYS);
        return token;
    }

    @Override
    public SysUser validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new TokenValidationException(ErrorCode.EMPTY_TOKEN_ERROR.getCode(), ErrorCode.EMPTY_TOKEN_ERROR.getMsg());
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            throw new TokenValidationException(ErrorCode.INVALID_TOKEN_ERROR.getCode(), ErrorCode.INVALID_TOKEN_ERROR.getMsg());
        }
        // fetch user authorization data
        // ex:
        //{\"account\":\"xxx\",\"avatar\":\"/static/img/xxx.png\",\"id\":x,\"nickname\":\""}
        String userDataJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userDataJson)) {
            throw new TokenValidationException(ErrorCode.INVALID_TOKEN_ERROR.getCode(), ErrorCode.INVALID_TOKEN_ERROR.getMsg());
        }
        return JSON.parseObject(userDataJson, SysUser.class);
    }

}
