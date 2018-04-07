package com.leemin.genealogy.service;

import com.leemin.genealogy.facebook.domain.AccessToken;
import com.leemin.genealogy.facebook.domain.UserDetails;
import com.leemin.genealogy.model.UserModel;
import org.springframework.social.facebook.api.User;

public interface UserService {
	UserModel findUserByEmail(String email);
	void saveUser(UserModel user,String role);
	void saveUser(UserModel user);

	void saveUserSocial(User userSocial,AccessToken accessToken);
}
