package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.workbench.dao.ActivityDao;
import com.cwk.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


}
