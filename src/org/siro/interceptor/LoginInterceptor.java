package org.siro.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
	//登录检查,实现接口里的这个方法
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//判断session中是否有"User"的key
		HttpSession session = request.getSession();
		Object user = session.getAttribute("User");
		if (user == null){
			//跳转到登录页面
			response.sendRedirect("/index");
			return  false;
		}
		return true;
	}

}
