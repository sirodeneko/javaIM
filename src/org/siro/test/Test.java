package org.siro.test;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Test {
	private static ConcurrentHashMap<String, WebSocketSession> users;  //Map来存储WebSocketSession，key用USER_ID 即在线用户列表
	private static ConcurrentHashMap<Integer, CopyOnWriteArraySet<Integer>> rooms;


	static {
		// 线程安全map
		// id : session
		users =  new ConcurrentHashMap<String, WebSocketSession>();
		// roomId : idList
		rooms = new ConcurrentHashMap<Integer, CopyOnWriteArraySet<Integer>>();
	}
	public static void main(String[] args) {

		System.out.println(rooms.get(1));
		rooms.put(1,new CopyOnWriteArraySet<Integer>(Collections.singleton(1)));
		rooms.get(1).add(2);
		rooms.get(1).add(3);
		rooms.get(1).add(4);
		rooms.get(1).add(5);
		for (int userId : rooms.get(1)){
			System.out.println(userId);
		}

//		//获取加载配置管理类
//		Configuration configuration = new Configuration();
//
//		//不给参数就默认加载hibernate.cfg.xml文件，
//		configuration.configure();
//
//		//创建Session工厂对象
//		SessionFactory factory = configuration.buildSessionFactory();
//
//		//得到Session对象
//		Session session = factory.openSession();
//
//		//使用Hibernate操作数据库，都要开启事务,得到事务对象
//		Transaction transaction = session.getTransaction();
//
//		//开启事务
//		transaction.begin();
//
//		String hql ="delete from UserAndGroup as uag where uag.userID=:userID and uag.groupID=:groupID";
//		Query q = session.createQuery(hql);
//
//		q.setParameter("userID", 1);
//		q.setParameter("groupID", 3);
//		q.executeUpdate();
//		//提交事务
//		transaction.commit();
//
//		//关闭Session
//		session.close();

	}
}