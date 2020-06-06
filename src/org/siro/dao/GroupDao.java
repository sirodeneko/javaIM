package org.siro.dao;

import org.siro.entity.Groups;
import org.siro.entity.User;
import org.siro.entity.UserAndGroup;

public interface GroupDao {
	// 创建
	public Groups createGroup(Groups groups);
	// 通过唯一id查询
	public Groups findGroup(int id);
	// 通过userID查询属于（创建的）他的房间
	public Groups[] findGroups(int id);
	// 通过房间名查询房间
	public Groups[] findGroups(String name);
	// 通过房间号查询用户
	public User[] findUsers(int id);
	// 通过用户id查询房间
	public Groups[] findMyGroups(int id);
	// 通过模糊查询查询房间
	public Groups[] vagueFindGroups(String name);
	// 加入房间
	public UserAndGroup joinGroup(UserAndGroup userAndGroup);
	// 退出房间
	public UserAndGroup exitGroup(UserAndGroup userAndGroup);
}
