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

        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        for(DicType dicType:dicTypeList){
            String code = dicType.getCode();
            List<DicValue> dicValueList = dicValueDao.getDicValueList(code);
            map.put(code,dicValueList);
        }
        return map;
    }
}
