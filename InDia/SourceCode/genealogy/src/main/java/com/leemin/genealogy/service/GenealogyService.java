package com.leemin.genealogy.service;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.GenealogyPedigreeModel;

import java.util.List;

public interface GenealogyService {

    List<GenealogyModel> findAll();

    List<GenealogyModel> findAllByUserName(String username);

    List<GenealogyModel> findAllLike(String search);

    GenealogyModel find(long idGenealogy,String username);

    boolean create(String name,String history,String thuyTo, String username);

    GenealogyModel create(GenealogyModel genealogyModel,String usernameOrEmal);

    GenealogyModel update(GenealogyModel genealogyModel);

    GenealogyModel findById(long id);

    boolean delete(GenealogyModel genealogyModel);

    boolean delete(long id);


}
