package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface RoleRepository extends JpaRepository<RoleModel, Integer> {
	RoleModel findByRole(String role);

}
