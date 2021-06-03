package com.cwk.crm.workbench.dao;

import com.cwk.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);
}
