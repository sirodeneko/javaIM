package org.siro.service;

import org.siro.entity.User;


public interface UserService {
	public User findUser(User user);
	public User createUser(User user);
}
