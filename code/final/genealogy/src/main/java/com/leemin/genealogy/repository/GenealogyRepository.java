package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PermissionModel;
import com.leemin.genealogy.model.RoleModel;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GenealogyRepository extends JpaRepository<GenealogyModel, Long> {

        List<GenealogyModel> findAll(Sort sort);

        List<GenealogyModel> findAll(Iterable<Long> iterable);

        Page<GenealogyModel> findAll(Pageable pageable);
}