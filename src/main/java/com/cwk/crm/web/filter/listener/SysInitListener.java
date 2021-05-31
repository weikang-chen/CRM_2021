package com.cwk.crm.web.filter.listener;

import com.cwk.crm.settings.damain.DicValue;
import com.cwk.crm.settings.service.DicService;
import com.cwk.crm.settings.service.impl.DicServiceImpl;
import com.cwk.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set) {
            application.setAttribute("key",map.get(key));
        }

        System.out.println("上下文域对象创建");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
