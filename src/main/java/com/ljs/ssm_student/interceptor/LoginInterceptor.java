package com.ljs.ssm_student.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String name = (String) request.getSession().getAttribute("name");
        if (!StringUtils.isEmpty(name)){
            return true;
        }else {
            String xmlHttpRequest = request.getHeader("X-Requested-With");
            if (StringUtils.equals(xmlHttpRequest,"XMLHttpRequest")){
                Map map = new HashMap<String,String>();
                map.put("type","error");
                map.put("msg","登录信息已失效");
                response.getWriter().write(JSONObject.wrap(map).toString());
                return false;
            }
            response.sendRedirect(request.getContextPath() + "/system/login");
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
