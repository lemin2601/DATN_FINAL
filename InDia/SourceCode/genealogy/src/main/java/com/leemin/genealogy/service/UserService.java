package com.leemin.genealogy.service;

import com.leemin.genealogy.facebook.domain.AccessToken;
import com.leemin.genealogy.facebook.domain.UserDetails;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.social.facebook.api.User;

import java.awt.print.Book;
import java.util.List;

public interface UserService {
	UserModel findUserByEmail(String email);
	UserModel findUserById(long id);
	void saveUser(UserModel user,String role);
	void saveUser(UserModel user);
	void saveUserSocial(User userSocial,AccessToken accessToken);

	void delete(long id);
	UserModel findOne(int id);
	void save(UserModel user);
	List<UserModel> findAll();

	List<UserModel> findAllBy();

}
