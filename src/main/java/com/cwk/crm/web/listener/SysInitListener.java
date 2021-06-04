package com.cwk.crm.web.listener;

import com.cwk.crm.settings.domain.DicValue;
import com.cwk.crm.settings.service.DicService;
import com.cwk.crm.settings.service.impl.DicServiceImpl;
import com.cwk.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set) {
            application.setAttribute(key,map.get(key));
        }

        System.out.println("上下文域对象创建");

        //----------------------------
        Map<String,String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
