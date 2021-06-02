package com.cwk.crm.workbench.dao;

import com.cwk.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityByClueId(String clueId);

    List<Activity> getActivityByNameAndClueId(Map<String, Object> map);
}
