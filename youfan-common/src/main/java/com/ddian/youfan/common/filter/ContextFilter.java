package com.ddian.youfan.common.filter;

import com.ddian.youfan.common.Constants.CommonConstants;
import com.ddian.youfan.common.context.FilterContextHandler;
import com.ddian.youfan.common.dto.UserToken;
import com.ddian.youfan.common.utils.JsonUtils;
import com.ddian.youfan.common.utils.JwtUtils;
import com.ddian.youfan.common.utils.R;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        if(request.getRequestURI().startsWith("/login")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = request.getHeader(CommonConstants.CONTEXT_TOKEN);
        UserToken userToken = null;
        try {
            userToken = JwtUtils.getInfoFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JsonUtils.toJson(R.error(403,"缺少token,非法请求")));
            return;
        }
        FilterContextHandler.setToken(token);
        FilterContextHandler.setUsername(userToken.getUsername());
        FilterContextHandler.setName(userToken.getName());
        FilterContextHandler.setUserID(userToken.getUserId());
        FilterContextHandler.setDeptID(userToken.getDeptId());
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}