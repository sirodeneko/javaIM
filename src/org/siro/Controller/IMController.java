package org.siro.Controller;

import org.siro.entity.User;
import org.siro.service.UserService;
import org.siro.service.impl.UserServiceImpl;
import org.siro.tools.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IMController {
	@Autowired
	private UserServiceImpl userServiceImpl;

	public void setUserService(UserService userService) {
		this.userServiceImpl = userServiceImpl;
	}

	@RequestMapping("index")
	public String welcome(){
		return "login";
	}

	@RequestMapping("IM")
	public String IM(){
		return "success";
	}

	@RequestMapping("Login")
	public  String Login(@ModelAttribute User user, HttpSession session, Model model){
		if(user.getName()==null||user.getName()==""||user.getPwd()==""||user.getPwd()==null){
			model.addAttribute("msg", "用户名或密码错误，请重新登录！");
			return "login";
		}

		User user1 = userServiceImpl.findUser(user);
		if(user1==null){
			user1=userServiceImpl.createUser(user);
			session.setMaxInactiveInterval(60*24*30);
			session.setAttribute("User", user1);
			return "redirect:/IM";

		}else if(SHA256.JudgeString(user.getPwd(),user1.getPwd())){
			session.setMaxInactiveInterval(60*24*30);
			session.setAttribute("User", user1);
			return "redirect:/IM";
		}else{
			model.addAttribute("msg", "用户名或密码错误，请重新登录！");
			return "login";
		}
	}
	@RequestMapping("LoginOut")
	public  String LoginOut(HttpSession session){
		session.removeAttribute("User");
		return "login";
	}
}
