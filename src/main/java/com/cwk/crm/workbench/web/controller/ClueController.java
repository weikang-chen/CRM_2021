package com.cwk.crm.workbench.web.controller;

import com.cwk.crm.settings.damain.User;
import com.cwk.crm.settings.service.UserService;
import com.cwk.crm.settings.service.impl.UserServiceImpl;
import com.cwk.crm.utils.DateTimeUtil;
import com.cwk.crm.utils.PrintJson;
import com.cwk.crm.utils.ServiceFactory;
import com.cwk.crm.utils.UUIDUtil;
import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.domain.Activity;
import com.cwk.crm.workbench.domain.ActivityRemark;
import com.cwk.crm.workbench.service.ActivityService;
import com.cwk.crm.workbench.service.Impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if("/workbench/activity/getUserList.do".equals(path)){
            //(request,response);
        }
    }




}
