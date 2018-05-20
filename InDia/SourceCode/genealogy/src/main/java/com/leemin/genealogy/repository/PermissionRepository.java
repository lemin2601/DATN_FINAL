package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
}
