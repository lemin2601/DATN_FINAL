package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PedigreeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedigreeRepository extends JpaRepository<PedigreeModel, Long> {



    List<PedigreeModel> findAll();

    List<PedigreeModel> findAll(Sort sort);

    List<PedigreeModel> findAll(Iterable<Long> iterable);

    Page<PedigreeModel> findAll(Pageable pageable);
}