package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface UserRepository extends JpaRepository<UserModel, Long> {
	 UserModel findByEmail(String email);

	 UserModel findById(long id);
	//crud
//	<S extends UserModel> S save(S entity);
//
//	UserModel findOne(Long primaryKey);
//
//	List<UserModel> findAll();
//
//	long count();
//
//	void delete(UserModel entity);
//
//	boolean exists(Long primaryKey);
//
//	//PagingAndSortingRepository
//	@Override
//	List<UserModel> findAll(Sort sort);

	Page<UserModel> findAll(Pageable pageable);


}
