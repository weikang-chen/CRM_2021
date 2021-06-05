package com.cwk.crm.workbench.service;

import com.cwk.crm.workbench.domain.Tran;
import com.cwk.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);
}
