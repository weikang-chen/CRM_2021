package com.cwk.crm.workbench.service;

import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {


    boolean save(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);
}
