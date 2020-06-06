package org.siro.handler;


import com.alibaba.fastjson.JSON;
import org.siro.entity.ChatMessage;
import org.siro.entity.Groups;
import org.siro.entity.User;
import org.siro.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class SpringWebSocketHandler extends TextWebSocketHandler {

	private static Logger logger = LoggerFactory.getLogger(SpringWebSocketHandler.class);

	private static ConcurrentHashMap<String, WebSocketSession> users;  //Map来存储WebSocketSession，key用USER_ID 即在线用户列表
	private static ConcurrentHashMap<Integer, CopyOnWriteArraySet<Integer>> rooms;

	//用户标识
	private static final String USER_ID = "WEBSOCKET_USERID";   //对应监听器从的key

	@Autowired
	GroupService groupService;

	static {
		// 线程安全map
		// id : session
		users =  new ConcurrentHashMap<String, WebSocketSession>();
		// roomId : idList
		rooms = new ConcurrentHashMap<Integer,CopyOnWriteArraySet<Integer>>();

	}

	public SpringWebSocketHandler() {}

	/**
	 * 连接成功时候，会触发页面上onopen方法
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		//System.out.println("成功建立websocket连接!");
		User user = (User) session.getAttributes().get(USER_ID);
		if(users.get(Integer.toString(user.getId()))!=null){
			ChatMessage sendMsg = new ChatMessage();
			sendMsg.setUser(user);
			sendMsg.setType(ChatMessage.MessageType.Other);
			sendMsg.setContent("已在别处上线！！！");
			sendMessageToUser(Integer.toString(user.getId()), new TextMessage(JSON.toJSONString(sendMsg)));
			users.get(Integer.toString(user.getId())).close();
			users.put(Integer.toString(user.getId()),session);
			// 用户重复上线 因为已经存rooms集合了，不需要再次处理
		}else{
			users.put(Integer.toString(user.getId()),session);
			// 新用户上线加入房间
			// 先查询所在房间
			Groups[] groups=groupService.findMyGroups(user.getId());
			for (Groups group:groups) {
				// 如果这个房间为不为空 就将用户加入房间id对应的set集合，如果为空就新建一个键值对
				if(rooms.get(group.getId())!=null){
					rooms.get(group.getId()).add(user.getId());
				}else {
					rooms.put(group.getId(),new CopyOnWriteArraySet<Integer>(Collections.singleton(user.getId())));
				}

			}
		}


		//System.out.println("当前线上用户数量:"+users.size());

		//这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
		//TextMessage returnMessage = new TextMessage("成功建立socket连接，你将收到的离线");
		//session.sendMessage(returnMessage);
	}

	/**
	 * 关闭连接时触发
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		User user= (User) session.getAttributes().get(USER_ID);
		//System.out.println("用户"+user.getName()+"已退出！");
		// 特殊处理下，防止同一账号上线时误清除session
		if(users.get(Integer.toString(user.getId()))==session){
			users.remove(Integer.toString(user.getId()));

			Groups[] groups=groupService.findMyGroups(user.getId());
			for (Groups group:groups) {
				if(rooms.get(group.getId())!=null){
					rooms.get(group.getId()).remove(user.getId());
				}

			}

		}
		//System.out.println("剩余在线用户"+users.size())
		System.out.println("2.close!");
	}

	/**
	 * js调用websocket.send时候，会调用该方法
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		super.handleTextMessage(session, message);

		ChatMessage putMsg = JSON.parseObject(message.getPayload(), ChatMessage.class);
		//System.out.println("服务器收到消息："+message);
		User user=(User)session.getAttributes().get(USER_ID);
		ChatMessage sendMsg = new ChatMessage();
		sendMsg.setUser(user);
		switch (putMsg.getType()){
			case Ping:
				sendMsg.setType(ChatMessage.MessageType.Pong);
				sendMsg.setContent("succcess!");
				sendMessageToUser(Integer.toString(user.getId()), new TextMessage(JSON.toJSONString(sendMsg))) ;
				break;
			case Msg:
				sendMsg.setType(ChatMessage.MessageType.Msg);
				sendMsg.setContent(putMsg.getContent());
				sendMsg.setTo(putMsg.getTo());
				sendMessageToGroup(sendMsg.getTo(),new TextMessage(JSON.toJSONString(sendMsg)));
				break;
			case Pic:
				sendMsg.setType(ChatMessage.MessageType.Pic);
				sendMsg.setContent(putMsg.getContent());
				sendMsg.setTo(putMsg.getTo());
				sendMessageToGroup(sendMsg.getTo(),new TextMessage(JSON.toJSONString(sendMsg)));
				break;
			default:
				sendMsg.setContent("未知指令");
				sendMsg.setType(ChatMessage.MessageType.Err);
				sendMessageToUser(Integer.toString(user.getId()),new TextMessage(JSON.toJSONString(sendMsg))) ;
		}


	}

	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("1.err\n");
		exception.printStackTrace();
//		if(session.isOpen()){
//			session.close();
//		}
//		logger.debug("传输出现异常，关闭websocket连接... ");
//		String userId= Integer.toString(((User) session.getAttributes().get(USER_ID)).getId());
//		users.remove(userId);
	}

	public boolean supportsPartialMessages() {

		return false;
	}


	/**
	 * 给某个用户发送消息
	 *
	 * @param userId
	 * @param message
	 */
	public void sendMessageToUser(String userId, TextMessage message) {
		try {
			if (users.get(userId).isOpen()) {
				users.get(userId).sendMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给所有在线用户发送消息
	 *
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		for (String userId : users.keySet()) {
			try {
				if (users.get(userId).isOpen()) {
					users.get(userId).sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessageToGroup(int to,TextMessage message) {
		// 从rooms拿出userID进行广播
		for (int userId : rooms.get(to)){
			try {
				if (users.get(Integer.toString(userId)).isOpen()) {
					users.get(Integer.toString(userId)).sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		for (String userId : users.keySet()) {
//			try {
//				if (users.get(userId).isOpen()) {
//					users.get(userId).sendMessage(message);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}