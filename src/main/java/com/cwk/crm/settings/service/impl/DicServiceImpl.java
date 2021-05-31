package com.cwk.crm.settings.service.impl;

import com.cwk.crm.settings.damain.DicType;
import com.cwk.crm.settings.damain.DicValue;
import com.cwk.crm.settings.dao.DicTypeDao;
import com.cwk.crm.settings.dao.DicValueDao;
import com.cwk.crm.settings.service.DicService;
import com.cwk.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        List<DicType> dicTypeList = dicTypeDao.getDicTypeList();
        List<DicValue> dicValueList = dicValueDao.getDicValueList();
        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        return map;
    }
}
