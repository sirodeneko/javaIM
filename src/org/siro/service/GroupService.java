package org.siro.service;

import org.siro.entity.Groups;
import org.siro.entity.User;

public interface GroupService {
	// 创建
	public Groups createGroup(Groups groups);
	// 通过唯一id查询
	public Groups findGroup(int id);
	// 通过房间创建者查询房间
	public Groups[] findGroups(int id);
	// 通过房间名查询房间
	public Groups[] findGroups(String name);
	// 通过房间号查询用户
	public User[] findUsers(int id);
	// 通过用户id查询房间
	public Groups[] findMyGroups(int id);

	public Groups[] vagueFindGroups(String name);

	public Groups joinGroup(User user,int to);

	public Groups exitGroup(User user,int to);
}
