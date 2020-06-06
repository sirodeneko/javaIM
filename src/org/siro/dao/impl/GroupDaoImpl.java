package org.siro.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.siro.dao.GroupDao;
import org.siro.entity.Groups;
import org.siro.entity.User;
import org.siro.entity.UserAndGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Repository
public class GroupDaoImpl implements GroupDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Groups createGroup(Groups groups) {
		try{
			// 创建房间
			sessionFactory.getCurrentSession().save(groups);
			// 加入房间
			UserAndGroup userAndGroup=new UserAndGroup();
			userAndGroup.setGroupID(groups.getId());
			userAndGroup.setUserID(groups.getMaster());
			sessionFactory.getCurrentSession().save(userAndGroup);
		}catch (Exception e){
			return null;
		}
		return groups;
	}

	@Override
	public Groups findGroup(int id) {
		String hql ="from Groups g  where g.id=:id";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("id", id);
		return (Groups)q.uniqueResult();
	}



	@Override
	public Groups[] findGroups(int id) {
		String hql ="from Groups g  where g.master=:master";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("master", id);
		ArrayList lists = (ArrayList) q.list();
		Groups[] groups= new Groups[lists.size()];
		for (int i=0;i<lists.size();i++) {
			groups[i]=(Groups) lists.get(i);
		}
		return groups;
	}

	@Override
	public Groups[] findGroups(String name) {
		String hql ="from Groups g  where g.id=:name";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", name);
		ArrayList lists = (ArrayList) q.list();
		Groups[] groups= new Groups[lists.size()];
		for (int i=0;i<lists.size();i++) {
			groups[i]=(Groups) lists.get(i);
		}
		return groups;
	}

	@Override
	public User[] findUsers(int id) {

		String hql ="select u from User as u,UserAndGroup as uag where uag.groupID=:groupID and u.id=uag.userID";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("groupID", id);
		ArrayList lists = (ArrayList) q.list();
		User[] users= new User[lists.size()];
		for (int i=0;i<lists.size();i++) {
			users[i]=(User) lists.get(i);
		}
		return users;
	}

	@Override
	public Groups[] findMyGroups(int id) {
		String hql ="select g from Groups as g,UserAndGroup as uag where uag.userID=:userID and g.id=uag.groupID";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userID", id);
		ArrayList lists = (ArrayList) q.list();
		Groups[] groups= new Groups[lists.size()];
		for (int i=0;i<lists.size();i++) {
			groups[i]=(Groups) lists.get(i);
		}
		return groups;
	}

	@Override
	public Groups[] vagueFindGroups(String name) {
		String hql ="from Groups as g where g.name like :name";
		Query q = sessionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", "%"+name+"%");
		ArrayList lists = (ArrayList) q.list();
		Groups[] groups= new Groups[lists.size()];
		for (int i=0;i<lists.size();i++) {
			groups[i]=(Groups) lists.get(i);
		}
		return groups;
	}

	@Override
	public UserAndGroup joinGroup(UserAndGroup userAndGroup) {
		try{
			// 创建房间
			sessionFactory.getCurrentSession().save(userAndGroup);
		}catch (Exception e){
			return null;
		}
		return userAndGroup;
	}

	@Override
	public UserAndGroup exitGroup(UserAndGroup userAndGroup) {
		try{
			//sessionFactory.getCurrentSession().delete(userAndGroup);
			String hql ="delete from UserAndGroup as uag where uag.userID=:userID and uag.groupID=:groupID";
			Query q = sessionFactory.getCurrentSession().createQuery(hql);
			q.setParameter("userID", userAndGroup.getUserID());
			q.setParameter("groupID", userAndGroup.getGroupID());
			q.executeUpdate();
		}catch (Exception e){
			return null;
		}
		return userAndGroup;
	}
}
