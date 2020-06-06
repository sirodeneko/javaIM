package org.siro.dao;

import org.siro.entity.User;

public interface UserDao {
	public User findUser(User user);
	public User createUser(User user);
}
