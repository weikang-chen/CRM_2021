package com.cwk.crm.workbench.dao;


import com.cwk.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbundById(String id);


    int bund(List<ClueActivityRelation> carList);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(String clueId);
}
