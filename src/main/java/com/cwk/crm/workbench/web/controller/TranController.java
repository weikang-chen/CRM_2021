package com.cwk.crm.workbench.web.controller;

import com.cwk.crm.settings.domain.User;
import com.cwk.crm.settings.service.UserService;
import com.cwk.crm.settings.service.impl.UserServiceImpl;
import com.cwk.crm.utils.DateTimeUtil;
import com.cwk.crm.utils.PrintJson;
import com.cwk.crm.utils.ServiceFactory;
import com.cwk.crm.utils.UUIDUtil;
import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.domain.Clue;
import com.cwk.crm.workbench.domain.Customer;
import com.cwk.crm.workbench.domain.Tran;
import com.cwk.crm.workbench.service.ActivityService;
import com.cwk.crm.workbench.service.ClueService;
import com.cwk.crm.workbench.service.CustomerService;
import com.cwk.crm.workbench.service.Impl.ActivityServiceImpl;
import com.cwk.crm.workbench.service.Impl.ClueServiceImpl;
import com.cwk.crm.workbench.service.Impl.CustomerServiceImpl;
import com.cwk.crm.workbench.service.Impl.TranServiceImpl;
import com.cwk.crm.workbench.service.TranService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t,customerName);

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subName = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> nameList = cs.getCustomerName(subName);
        PrintJson.printJsonObj(response,nameList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }

}
