package org.siro.Controller;

import com.alibaba.fastjson.JSON;
import org.siro.entity.Groups;
import org.siro.entity.ImgGet;
import org.siro.entity.User;
import org.siro.handler.SpringWebSocketHandler;
import org.siro.service.GroupService;
import org.siro.service.impl.OSSServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
@Controller
@RequestMapping("/api")
public class JsonApiController {

	@Autowired
	OSSServiceImpl ossService;

	@Autowired
	GroupService groupService;

	@ResponseBody
	@RequestMapping(value = "/me",produces = "application/json; charset=utf-8")
	public String json(HttpSession session){
		User user=(User)session.getAttribute("User");
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		responseBody.put("code",200);
		responseBody.put("User",user);
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/putImg",produces = "application/json; charset=utf-8")
	public String putImg(String fileName){
		ImgGet imgGet=ossService.getUrl(fileName);
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		responseBody.put("code",200);
		responseBody.put("img",imgGet);
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/myGroups",produces = "application/json; charset=utf-8")
	public String myGroups(String id){
		Groups[] groups=groupService.findMyGroups(Integer.parseInt(id));
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		responseBody.put("code",200);
		responseBody.put("groups",groups);
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/GroupUsers",produces = "application/json; charset=utf-8")
	public String GroupUsers(String id){
		User[] groups=groupService.findUsers(Integer.parseInt(id));
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		responseBody.put("code",200);
		responseBody.put("groups",groups);
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/SearchGroup",produces = "application/json; charset=utf-8")
	public String SearchGroup(String name){
		Groups[] groups=groupService.vagueFindGroups(name);
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		responseBody.put("code",200);
		responseBody.put("groups",groups);
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/CreateGroup",produces = "application/json; charset=utf-8")
	public String SearchGroup(String name,HttpSession session){
		User user=(User)session.getAttribute("User");
		Groups groups= new Groups();
		groups.setCreateTime(new Date());
		groups.setMaster(user.getId());
		groups.setName(name);
		groups=groupService.createGroup(groups);
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		if(groups==null){
			responseBody.put("code",500);
			responseBody.put("err","创建失败");
		}else{
			responseBody.put("code",200);
			responseBody.put("groups",groups);
			SpringWebSocketHandler.addRoom(user.getId(),groups.getId());
		}
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/JoinGroup",produces = "application/json; charset=utf-8")
	public String JoinGroup(int to,HttpSession session){
		User user=(User)session.getAttribute("User");
		Groups groups= groupService.joinGroup(user,to);
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		if(groups==null){
			responseBody.put("code",500);
			responseBody.put("err","加入失败，服务器错误或者已经加入");
		}else{
			responseBody.put("code",200);
			responseBody.put("groups",groups);
			SpringWebSocketHandler.addRoom(user.getId(),groups.getId());
		}
		return JSON.toJSONString(responseBody);
	}

	@ResponseBody
	@RequestMapping(value = "/ExitGroup",produces = "application/json; charset=utf-8")
	public String ExitGroup(int to,HttpSession session){
		User user=(User)session.getAttribute("User");
		Groups groups= groupService.exitGroup(user,to);
		HashMap<String,Object> responseBody = new HashMap<String, Object>();
		if(groups==null){
			responseBody.put("code",500);
			responseBody.put("err","退出失败");
		}else{
			responseBody.put("code",200);
			responseBody.put("groups",groups);
			SpringWebSocketHandler.delRoom(user.getId(),groups.getId());
		}
		return JSON.toJSONString(responseBody);
	}
}
