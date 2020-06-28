package org.siro.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.siro.dao.UserDao;
import org.siro.entity.User;
import org.siro.entity.UserAndGroup;
import org.siro.tools.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User findUser(User user) {
		String hql ="from User u  where u.name=:name";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", user.getName());
		return (User)q.uniqueResult();
	}

	@Override
	public User createUser(User user) {
		try{
			// 新建用户
			user.setPwd(SHA256.GetSHAString(user.getPwd()));
			sessionFactory.getCurrentSession().save(user);
			// 自动加入聊天室1
			UserAndGroup userAndGroup=new UserAndGroup();
			userAndGroup.setGroupID(1);
			userAndGroup.setUserID(user.getId());
			sessionFactory.getCurrentSession().save(userAndGroup);
		}catch (Exception e){
			return null;
		}
		return user;
	}
}
