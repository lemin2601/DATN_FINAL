package com.leemin.genealogy.service;

import com.leemin.genealogy.facebook.domain.AccessToken;
import com.leemin.genealogy.facebook.domain.UserDetails;
import com.leemin.genealogy.model.RoleModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.model.UserTokenModel;
import com.leemin.genealogy.repository.RoleRepository;
import com.leemin.genealogy.repository.UserRepository;
import com.leemin.genealogy.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

@Service()
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository      userRepository;
	@Autowired
    private RoleRepository      roleRepository;
	@Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserModel findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(UserModel user,String role) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        RoleModel userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<RoleModel>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public void saveUser(UserModel user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		RoleModel userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<RoleModel>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public void saveUserSocial(User userSocial,AccessToken accessToken) {
		UserModel userModel = userRepository.findByEmail(userSocial.getEmail());
		if(userModel == null){
			UserModel user = new UserModel();
			user.setEmail(userSocial.getEmail());
			user.setName(userSocial.getName());
			user.setLastName(userSocial.getLastName());
//		user.setBirthday(userSocial.getBirthday());
			LinkedHashMap picture = (LinkedHashMap)userSocial.getExtraData().get("picture");
			LinkedHashMap dataPicture =(LinkedHashMap) picture.get("data");
			user.setImg((String)dataPicture.get("url"));
			user.setLink(userSocial.getLink());
			user.setGender(userSocial.getGender());
			user.setFirstName(userSocial.getFirstName());
			RoleModel userRole = roleRepository.findByRole("USER_FB");
			user.setRoles(new HashSet<RoleModel>(Arrays.asList(userRole)));
			user.setPassword(bCryptPasswordEncoder.encode(accessToken.getAccess_token()));
			user.setActive(1);
			userRepository.save(user);
			userModel = userRepository.findByEmail(userSocial.getEmail());
		}

		UserTokenModel userTokenModel = new UserTokenModel();
		userTokenModel.setSocialId(userSocial.getId());
		userTokenModel.setAccessToken(accessToken.getAccess_token());
		userTokenModel.setUserId(userModel.getId());
		userTokenRepository.save(userTokenModel);

	}
}
