package com.cwk.crm.settings.service.impl;

import com.cwk.crm.settings.dao.UserDao;
import com.cwk.crm.settings.service.UserService;
import com.cwk.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
