package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.GenealogyPedigreeModel;
import com.leemin.genealogy.model.PedigreeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenealogyPedigreeRepository extends JpaRepository<GenealogyPedigreeModel, Long> {


    List<GenealogyPedigreeModel> findAllByGenealogy_Id(long id);

//    List<GenealogyPedigreeModel> findByGenealogy_Id(long idGenealogy);

    @Query("SELECT p FROM GenealogyPedigreeModel p JOIN FETCH  p.genealogy JOIN FETCH p.pedigree WHERE p.genealogy.id = :idGenealogy")
    List<GenealogyPedigreeModel> findByGenealogy_Id(@Param("idGenealogy") long idGenealogy);

    @Query("SELECT p FROM GenealogyPedigreeModel p JOIN FETCH  p.genealogy JOIN FETCH p.pedigree WHERE p.genealogy.name LIKE :name")
    List<GenealogyPedigreeModel> findByLike(@Param("name") String search);


    GenealogyPedigreeModel findByGenealogy_IdAndPedigreeId(long idGenealogy,long idPedigree);

    List<GenealogyPedigreeModel> findAll(Sort sort);

    List<GenealogyPedigreeModel> findAll(Iterable<Long> iterable);

    Page<GenealogyPedigreeModel> findAll(Pageable pageable);
}