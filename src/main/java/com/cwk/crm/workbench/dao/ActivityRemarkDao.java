package com.cwk.crm.workbench.dao;

import com.cwk.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteById(String id);

    int save(ActivityRemark ar);
}
