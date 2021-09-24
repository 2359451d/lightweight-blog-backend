package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bento.blog.dao.mapper.SysUserMapper;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.service.AuthenticationService;
import top.bento.blog.service.SysUserService;
import top.bento.blog.vo.CommenterVo;
import top.bento.blog.vo.SysUserVo;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("default name");
        }
        return sysUser;
    }

    @Override
    public SysUser findUserByCredentials(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    /**
     * validate the given token
     * whether can be fetched in redis
     * if so, return the corresponding user authorization info
     * otherwise, throw ValidationException
     */
    @Override
    public SysUserVo findUserByToken(String token) {
        SysUser user = authenticationService.validateToken(token);
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(user, sysUserVo);
        return sysUserVo;
    }

    @Override
    public CommenterVo findCommenterById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            //commenter default value
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("default commenter");
        }
        CommenterVo commenterVo = new CommenterVo();
        BeanUtils.copyProperties(sysUser, commenterVo);
        return commenterVo;
    }

    /**
     * Using distributed id
     *
     * @param user
     */
    @Override
    public void save(SysUser user) {
        sysUserMapper.insert(user);
    }
}
