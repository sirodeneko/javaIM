package org.siro.service.impl;

import org.siro.dao.GroupDao;
import org.siro.entity.Groups;
import org.siro.entity.User;
import org.siro.entity.UserAndGroup;
import org.siro.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDao groupDao;

	@Override
	public Groups createGroup(Groups groups) {
		return groupDao.createGroup(groups);
	}

	@Override
	public Groups findGroup(int id) {
		return groupDao.findGroup(id);
	}

	@Override
	public Groups[] findGroups(int id) {
		return groupDao.findGroups(id);
	}

	@Override
	public Groups[] findGroups(String name) {
		return groupDao.findGroups(name);
	}

	@Override
	public User[] findUsers(int id) {
		return groupDao.findUsers(id);
	}

	@Override
	public Groups[] findMyGroups(int id) {
		return groupDao.findMyGroups(id);
	}

	@Override
	public Groups[] vagueFindGroups(String name) {
		return groupDao.vagueFindGroups(name);
	}

	@Override
	public Groups joinGroup(User user, int to) {
		Groups groups=groupDao.findGroup(to);
		if(groups!=null){
			UserAndGroup userAndGroup=new UserAndGroup();
			userAndGroup.setUserID(user.getId());
			userAndGroup.setGroupID(to);
			userAndGroup=groupDao.joinGroup(userAndGroup);
			if(userAndGroup!=null){
				return groups;
			}else {
				return null;
			}
		}else{
			return null;
		}
	}

	@Override
	public Groups exitGroup(User user, int to) {
		Groups groups=groupDao.findGroup(to);
		if(groups!=null){
			UserAndGroup userAndGroup=new UserAndGroup();
			userAndGroup.setUserID(user.getId());
			userAndGroup.setGroupID(to);
			userAndGroup=groupDao.exitGroup(userAndGroup);
			if(userAndGroup!=null){
				return groups;
			}else {
				return null;
			}
		}else{
			return null;
		}
	}
}
