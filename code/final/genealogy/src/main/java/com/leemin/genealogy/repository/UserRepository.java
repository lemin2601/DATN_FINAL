package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface UserRepository extends JpaRepository<UserModel, Long> {
	 UserModel findByEmail(String email);


}
