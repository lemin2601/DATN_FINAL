package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PermissionModel;
import com.leemin.genealogy.model.UserGenealogyModel;
import com.leemin.genealogy.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenealogyRepository extends JpaRepository<UserGenealogyModel, Long> {

        List<UserGenealogyModel> findAll(Sort sort);

//        List<UserGenealogyModel> findAllByIdUser(long idUser);

        List<UserGenealogyModel> findAll(Iterable<Long> iterable);

        Page<UserGenealogyModel> findAll(Pageable pageable);

        List<UserGenealogyModel> findByUser(UserModel userModel);

        List<UserGenealogyModel> findByUserAndGenealogy_Id(UserModel userModel,long idGenealogy);

        List<UserGenealogyModel> findByGenealogy_Id(long idGenealogy);

        UserGenealogyModel findTopByUserAndGenealogy_Id(UserModel userModel,long idGenealogy);



}