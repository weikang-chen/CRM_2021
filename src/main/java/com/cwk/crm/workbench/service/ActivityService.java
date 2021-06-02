package com.cwk.crm.workbench.service;

import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityByClueId(String clueId);

    List<Activity> getActivityByNameAndClueId(Map<String, Object> map);
}
