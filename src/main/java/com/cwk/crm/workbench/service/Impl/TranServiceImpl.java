package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.DateTimeUtil;
import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.utils.UUIDUtil;
import com.cwk.crm.workbench.dao.CustomerDao;
import com.cwk.crm.workbench.dao.TranDao;
import com.cwk.crm.workbench.dao.TranHistoryDao;
import com.cwk.crm.workbench.domain.Customer;
import com.cwk.crm.workbench.domain.Tran;
import com.cwk.crm.workbench.domain.TranHistory;
import com.cwk.crm.workbench.service.TranService;

import java.util.List;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(t.getCreateTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }
        }
        t.setCustomerId(customer.getId());
        int count1 = tranDao.save(t);
        if(count1 != 1){
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(t.getId());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setCreateTime(t.getCreateTime());
        tranHistory.setCreateBy(t.getCreateBy());
        tranHistory.setStage(t.getStage());
        tranHistory.setId(UUIDUtil.getUUID());
        int count2  = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(tranId);
        return tranHistoryList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count = tranDao.changeStage(t);
        if(count != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(t.getStage());
        tranHistory.setCreateBy(t.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setTranId(t.getId());
        int count1 = tranHistoryDao.save(tranHistory);
        if(count1 != 1){
            flag = false;
        }
        return flag;
    }
}
