package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.workbench.dao.TranDao;
import com.cwk.crm.workbench.dao.TranHistoryDao;
import com.cwk.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
}
