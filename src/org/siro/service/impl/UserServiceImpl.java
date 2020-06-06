package org.siro.service.impl;

import org.siro.dao.UserDao;
import org.siro.entity.User;
import org.siro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User findUser(User user) {

		return userDao.findUser(user);
	}

	@Override
	public User createUser(User user) {

		return userDao.createUser(user);
	}


}
