package com.cwk.crm.settings.service;

import com.cwk.crm.settings.damain.User;
import com.cwk.crm.exception.loginException;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws loginException;
}
