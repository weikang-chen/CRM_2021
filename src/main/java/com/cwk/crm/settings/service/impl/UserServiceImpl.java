package com.cwk.crm.settings.service.impl;

import com.cwk.crm.settings.domain.User;
import com.cwk.crm.settings.dao.UserDao;
import com.cwk.crm.exception.loginException;
import com.cwk.crm.settings.service.UserService;
import com.cwk.crm.utils.DateTimeUtil;
import com.cwk.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws loginException {
        Map<String,String> map = new HashMap();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //向dao传map
        User user = userDao.login(map);

        //验证账号密码
        if(user == null){

            throw new loginException("账号密码错误");
            //有异常，下面代码不执行
        }

        //验证失效时间
        String expireTime = user.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(sysTime) < 0){
            throw new loginException("账号失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new loginException("账号被锁定");
        }

        //验证IP
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new loginException("无效的IP");
        }

        //执行到这里说明用户正常
        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();
        return userList;
    }
}
