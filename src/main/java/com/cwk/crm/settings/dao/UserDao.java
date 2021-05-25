package com.cwk.crm.settings.dao;

import com.cwk.crm.settings.damain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
