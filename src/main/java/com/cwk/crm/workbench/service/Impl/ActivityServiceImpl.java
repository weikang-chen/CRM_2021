package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.settings.damain.User;
import com.cwk.crm.settings.dao.UserDao;
import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.dao.ActivityDao;
import com.cwk.crm.workbench.dao.ActivityRemarkDao;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.service.ActivityService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public boolean save(Activity a) {
        boolean flag = true;
        int count  = activityDao.save(a);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean success = true;
        //查询需要删除的备注条数
        int count1 = activityRemarkDao.getCountByAids(ids);
        //返回实际删除的备注的条数
        int count2 = activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){
            success = false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3!=ids.length){
            success = false;
        }
        return success;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> userList = userDao.getUserList();
        Activity activity = activityDao.getActivityById(id);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean success = true;
        int count = activityDao.update(activity);
        if(count != 1){
            success = false;
        }
        return success;
    }


}
