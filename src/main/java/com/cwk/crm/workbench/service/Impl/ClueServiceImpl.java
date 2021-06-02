package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.utils.UUIDUtil;
import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.dao.ClueActivityRelationDao;
import com.cwk.crm.workbench.dao.ClueDao;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.domain.Clue;
import com.cwk.crm.workbench.domain.ClueActivityRelation;
import com.cwk.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean save(Clue c) {
        boolean success = true;
        int count = clueDao.save(c);
        if(count != 1){
            success = false;
        }
        return success;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {

        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);
        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;

    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbundById(String id) {
        boolean success = true;
        int count = clueActivityRelationDao.unbundById(id);
        if(count != 1){
            success = false;
        }
        return success;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean success = true;
        List<ClueActivityRelation> carList = new ArrayList<ClueActivityRelation>();
        for(String aid:aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            carList.add(car);
        }
        int count = clueActivityRelationDao.bund(carList);
        if(count != aids.length){
            success = false;
        }
        return success;
    }


}
