package com.cwk.crm.workbench.service;

import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbundById(String id);


    boolean bund(String cid, String[] aids);
}
