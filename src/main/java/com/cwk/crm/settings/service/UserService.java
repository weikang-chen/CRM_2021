package com.cwk.crm.settings.service;

import com.cwk.crm.settings.damain.User;
import com.cwk.crm.exception.loginException;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws loginException;

    List<User> getUserList();
}
