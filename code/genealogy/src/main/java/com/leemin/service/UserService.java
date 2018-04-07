package com.leemin.service;

import com.leemin.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
