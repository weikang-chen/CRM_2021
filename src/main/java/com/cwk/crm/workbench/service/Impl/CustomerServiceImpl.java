package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.workbench.dao.CustomerDao;
import com.cwk.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String subName) {
        List<String> nameList = customerDao.getCustomerName(subName);
        return nameList;
    }
}
