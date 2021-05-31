package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.workbench.dao.ClueDao;
import com.cwk.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
}
