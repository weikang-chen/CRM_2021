package com.cwk.crm.web.filter;

import com.cwk.crm.settings.damain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path) || "/login.jsp".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if(user != null){
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}